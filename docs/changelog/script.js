// ═══════════════════════════════════════════════
// DATA
// ═══════════════════════════════════════════════
const DEFAULT_TAGS = [
  "Desenvolvimento Inicial",
  "Adicionado",
  "Alterado",
  "Removido",
  "Resolvido",
];
const DEFAULT_AREAS = [
  "Backend",
  "Frontend",
  "App Mobile",
  "QA",
  "Infraestrutura",
  "Análise de Dados",
  "Documentação"
];
let SUGGESTIONS = {
  "Desenvolvimento Inicial": {},
  "Adicionado": {},
  "Alterado": {},
  "Removido": {},
  "Resolvido": {},
};
// ═══════════════════════════════════════════════
// PERSISTÊNCIA DO DATA.JSON
// ═══════════════════════════════════════════════

/** Lê o data.json da pasta selecionada ou cria um se não existir. */
async function loadDataJson() {
  if (!dirHandle) return;
  try {
    const fileHandle = await dirHandle.getFileHandle("data.json", { create: false });
    const file = await fileHandle.getFile();
    const content = await file.text();
    SUGGESTIONS = JSON.parse(content);
    renderPanel(); // Atualiza a tela com os novos dados
  } catch (e) {
    if (e.name === 'NotFoundError') {
      // Se não existe, cria o arquivo com a estrutura atual de SUGGESTIONS
      await saveDataJson();
    } else {
      console.warn("Erro ao ler data.json:", e);
    }
  }
}

/** Salva o estado atual da variável SUGGESTIONS no data.json */
async function saveDataJson() {
  if (!dirHandle) return;
  try {
    const fileHandle = await dirHandle.getFileHandle("data.json", { create: true });
    const w = await fileHandle.createWritable();
    await w.write(JSON.stringify(SUGGESTIONS, null, 2));
    await w.close();
  } catch (e) {
    console.error("Erro ao salvar data.json:", e);
  }
}


// ═══════════════════════════════════════════════
// STATE
// ═══════════════════════════════════════════════
let tags = [...DEFAULT_TAGS];
let areas = [...DEFAULT_AREAS];
let selTag = null;
let selArea = null;
let checked = {};
let stackRows = [];
let versions = []; // in-memory, loaded from disk
let dirHandle = null;
let currentView = "editor";
let isDragging = false;
let dragMode = null;

// ═══════════════════════════════════════════════
// INDEXEDDB — persiste o handle da pasta
// ═══════════════════════════════════════════════
const IDB_NAME = "changelog_app_v1";
const IDB_STORE = "handles";

function openIDB() {
  return new Promise((res, rej) => {
    const r = indexedDB.open(IDB_NAME, 1);
    r.onupgradeneeded = (e) => e.target.result.createObjectStore(IDB_STORE);
    r.onsuccess = (e) => res(e.target.result);
    r.onerror = () => rej(r.error);
  });
}

async function idbGet(key) {
  try {
    const db = await openIDB();
    return new Promise((res, rej) => {
      const tx = db.transaction(IDB_STORE, "readonly");
      const req = tx.objectStore(IDB_STORE).get(key);
      req.onsuccess = () => res(req.result ?? null);
      req.onerror = () => rej(req.error);
    });
  } catch {
    return null;
  }
}

async function idbSet(key, value) {
  try {
    const db = await openIDB();
    return new Promise((res, rej) => {
      const tx = db.transaction(IDB_STORE, "readwrite");
      tx.objectStore(IDB_STORE).put(value, key);
      tx.oncomplete = res;
      tx.onerror = () => rej(tx.error);
    });
  } catch {
    /* silent */
  }
}

// ═══════════════════════════════════════════════
// FILE SYSTEM ACCESS API
// ═══════════════════════════════════════════════

/** Verifica/solicita permissão de leitura+escrita no handle salvo. */
async function ensurePermission() {
  if (!dirHandle) return false;
  try {
    let perm = await dirHandle.queryPermission({ mode: "readwrite" });
    if (perm === "granted") return true;
    perm = await dirHandle.requestPermission({ mode: "readwrite" });
    return perm === "granted";
  } catch {
    return false;
  }
}

/** Abre o seletor de pasta, persiste handle e carrega versões. */
async function selectFolder() {
  if (!window.showDirectoryPicker) {
    document.getElementById("warnBox").style.display = "block";
    return false;
  }
  try {
    dirHandle = await window.showDirectoryPicker({ mode: "readwrite" });
    await idbSet("dirHandle", dirHandle);
    await loadVersionsFromDir();
    await loadDataJson(); // <--- ADICIONE ESTA LINHA
    renderSidebarVersions();
    updateDirUI();
    return true;
  } catch (e) {
    if (e.name !== "AbortError") console.error("selectFolder:", e);
    return false;
  }
}

/** Inicialização: tenta restaurar handle do IDB e carregar versões. */
async function initDir() {
  if (!window.showDirectoryPicker) {
    document.getElementById("warnBox").style.display = "block";
    updateDirUI();
    return;
  }
  dirHandle = await idbGet("dirHandle");
  if (dirHandle && (await ensurePermission())) {
    await loadVersionsFromDir();
    await loadDataJson(); // <--- ADICIONE ESTA LINHA
    renderSidebarVersions();
  } else {
    dirHandle = null;
  }
  updateDirUI();
}

/** Lê todos os arquivos v*.md da pasta e popula o array versions[]. */
async function loadVersionsFromDir() {
  if (!dirHandle) return;
  versions = [];

  for await (const [name, handle] of dirHandle.entries()) {
    // Aceita somente arquivos tipo "v0.1.0.md" — ignora CHANGELOG.md
    if (handle.kind !== "file") continue;
    if (!/^v.+\.md$/.test(name) || name === "vCHANGELOG.md") continue;

    try {
      const file = await handle.getFile();
      const content = await file.text();

      // Extrai bloco de metadados: <!-- cl-meta:{...} -->
      const metaMatch = content.match(/^<!-- cl-meta:([\s\S]*?)-->/);
      let meta = {};
      if (metaMatch) {
        try {
          meta = JSON.parse(metaMatch[1].trim());
        } catch {
          /* ignore */
        }
      }

      // O markdown limpo é o conteúdo sem o comentário de meta
      const cleanMd = content
        .replace(/^<!-- cl-meta:[\s\S]*?-->\n?/, "")
        .trimStart();
      const ver = meta.ver || name.slice(1, -3);

      versions.push({
        ver,
        dt: meta.dt || "",
        name: meta.name || "",
        md: cleanMd,
        savedAt: meta.savedAt || new Date(file.lastModified).toISOString(),
        filename: name,
      });
    } catch (e) {
      console.warn("Erro ao ler", name, e);
    }
  }

  // Ordena: mais recente primeiro (por savedAt)
  versions.sort((a, b) => new Date(b.savedAt) - new Date(a.savedAt));
}

/**
 * Escreve um arquivo individual: v{ver}.md
 * O arquivo contém um comentário de meta no topo + o markdown da versão.
 */
async function writeVersionFile(entry) {
  const meta = {
    ver: entry.ver,
    dt: entry.dt,
    name: entry.name,
    savedAt: entry.savedAt,
  };
  const content = `<!-- cl-meta:${JSON.stringify(meta)} -->\n${entry.md}`;
  const fname = `v${entry.ver.replace(/[^a-zA-Z0-9.\-_]/g, "_")}.md`;

  const fh = await dirHandle.getFileHandle(fname, { create: true });
  const w = await fh.createWritable();
  await w.write(content);
  await w.close();
  return fname;
}

/**
 * Reconstrói o CHANGELOG.md cumulativo com TODAS as versões salvas,
 * ordenadas da mais recente para a mais antiga.
 * Este é o arquivo que vai direto para o seu projeto.
 */
async function writeCumulativeChangelog() {
  if (!dirHandle || versions.length === 0) return;

  const header =
    "# Histórico de Mudanças\n\n" +
    "Todas as mudanças notáveis serão documentadas neste arquivo.\n" +
    "---\n\n";

  // Versões já estão ordenadas (mais recente primeiro) em versions[]
  const body = versions.map((v) => v.md).join("\n\n---\n\n");

  const fh = await dirHandle.getFileHandle("CHANGELOG.md", { create: true });
  const w = await fh.createWritable();
  await w.write(header + body);
  await w.close();
}

/** Atualiza a badge de status na sidebar. */
function updateDirUI() {
  const badge = document.getElementById("dirBadge");
  const label = document.getElementById("dirLabel");
  if (dirHandle) {
    badge.classList.add("connected");
    label.textContent = `📁 ${dirHandle.name}`;
  } else {
    badge.classList.remove("connected");
    label.textContent = "Nenhuma pasta conectada";
  }
}

// ═══════════════════════════════════════════════
// VIEWS
// ═══════════════════════════════════════════════
function showView(v) {
  currentView = v;
  document.getElementById("view-editor").style.display =
    v === "editor" ? "block" : "none";
  document.getElementById("view-history").style.display =
    v === "history" ? "block" : "none";
  document.getElementById("nav-editor").className =
    "sb-item" + (v === "editor" ? " active" : "");
  document.getElementById("nav-history").className =
    "sb-item" + (v === "history" ? " active" : "");
  if (v === "history") renderHistory();
}

async function reloadHistory() {
  if (!dirHandle) {
    await selectFolder();
    return;
  }
  if (!(await ensurePermission())) {
    await selectFolder();
    return;
  }
  await loadVersionsFromDir();
  renderSidebarVersions();
  renderHistory();
}

// ═══════════════════════════════════════════════
// PILLS
// ═══════════════════════════════════════════════
function renderTagPills() {
  const row = document.getElementById("tagPills");
  row.innerHTML = "";
  tags.forEach((t) => {
    const p = document.createElement("button");
    p.className = "pill " + (selTag === t ? "sel" : "unsel");
    p.dataset.tag = DEFAULT_TAGS.includes(t) ? t : "custom-pill";
    p.textContent = t;
    p.onclick = () => {
      selTag = selTag === t ? null : t;
      renderTagPills();
      renderAreaPills();
      renderPanel();
    };
    row.appendChild(p);
  });
}

function renderAreaPills() {
  const row = document.getElementById("areaPills");
  row.innerHTML = "";
  areas.forEach((a) => {
    const p = document.createElement("button");
    p.className = "pill " + (selArea === a ? "sel" : "unsel");
    p.dataset.area = DEFAULT_AREAS.includes(a) ? a : "custom-pill";
    p.textContent = a;
    p.onclick = () => {
      selArea = selArea === a ? null : a;
      renderAreaPills();
      renderPanel();
    };
    row.appendChild(p);
  });
}

function addCustomTag() {
  const inp = document.getElementById("newTagInput");
  const v = inp.value.trim();
  if (!v || tags.includes(v)) return;
  tags.push(v);
  inp.value = "";
  renderTagPills();
}

function addCustomArea() {
  const inp = document.getElementById("newAreaInput");
  const v = inp.value.trim();
  if (!v || areas.includes(v)) return;
  areas.push(v);
  inp.value = "";
  renderAreaPills();
}

// ═══════════════════════════════════════════════
// PANEL
// ═══════════════════════════════════════════════
function getKey() {
  return selTag && selArea ? `${selTag}||${selArea}` : null;
}
function getCheckedSet() {
  const k = getKey();
  if (!k) return new Set();
  if (!checked[k]) checked[k] = new Set();
  return checked[k];
}
function getSuggestions() {
  return (selTag && selArea && SUGGESTIONS[selTag]?.[selArea]) || [];
}


function renderPanel() {
  const k = getKey();
  const title = k ? `${selTag} · ${selArea}` : "Selecione um tipo e uma área";
  document.getElementById("panelTitle").textContent = title;

  const list = document.getElementById("itemsList");
  const sugg = getSuggestions(); // Traz direto da estrutura oficial

  if (!k || sugg.length === 0) {
    list.innerHTML = `<div class="empty-state">${k ? "Nenhum item sugerido. Adicione abaixo." : "Selecione um tipo de mudança e uma área."}</div>`;
    updateSelCount();
    return;
  }

  const cs = getCheckedSet();
  // Renderizamos tudo. O botão de deletar "✕" agora aparece para todos os itens.
  list.innerHTML = sugg
    .map(
      (item, i) => `
    <div class="item-row" data-item="${escAttr(item)}" onmousedown="rowMouseDown(event,'${escAttr(item)}')" onmouseenter="rowMouseEnter(event,'${escAttr(item)}')">
      <input type="checkbox" id="ir_${i}" ${cs.has(item) ? "checked" : ""} onchange="toggleItem('${escAttr(item)}')">
      <label for="ir_${i}">${escHtml(item)}</label>
      <button class="del-item" onclick="delCustomItem('${escAttr(item)}')">✕</button>
    </div>`,
    )
    .join("");
  updateSelCount();
}

async function addCustomItem() {
  const inp = document.getElementById("customItemInput");
  const v = inp.value.trim();
  if (!v || !selTag || !selArea) return;

  // Garante que a estrutura existe antes de adicionar
  if (!SUGGESTIONS[selTag]) SUGGESTIONS[selTag] = {};
  if (!SUGGESTIONS[selTag][selArea]) SUGGESTIONS[selTag][selArea] = [];

  // Se o item não existe na lista, adiciona, marca o checkbox e salva no disco!
  if (!SUGGESTIONS[selTag][selArea].includes(v)) {
    SUGGESTIONS[selTag][selArea].push(v);
    getCheckedSet().add(v);
    await saveDataJson(); // Salva permanentemente no data.json
  }

  inp.value = "";
  renderPanel();
}

async function delCustomItem(item) {
  if (!selTag || !selArea) return;

  // Remove da lista em memória
  if (SUGGESTIONS[selTag]?.[selArea]) {
    SUGGESTIONS[selTag][selArea] = SUGGESTIONS[selTag][selArea].filter(
      (x) => x !== item,
    );
    await saveDataJson(); // Atualiza o arquivo no disco
  }

  getCheckedSet().delete(item);
  renderPanel();
}

function toggleItem(item, force) {
  const cs = getCheckedSet();
  if (force === true) cs.add(item);
  else if (force === false) cs.delete(item);
  else cs.has(item) ? cs.delete(item) : cs.add(item);

  const all = getSuggestions(); // <--- CORRIGIDO AQUI (removido o spread com getCustom)

  const i = all.indexOf(item);
  if (i >= 0) {
    const cb = document.getElementById("ir_" + i);
    if (cb) cb.checked = cs.has(item);
  }
  updateSelCount();
}

function updateSelCount() {
  const k = getKey();
  const n = k ? getCheckedSet().size : 0;
  document.getElementById("selCount").textContent =
    n > 0 ? `${n} selecionado${n > 1 ? "s" : ""}` : "";
}

function selectAllInPanel() {
  // Pega todos os itens da área/tag selecionada direto das sugestões
  const all = getSuggestions();
  const cs = getCheckedSet();
  all.forEach((i) => cs.add(i));
  renderPanel();
}

function selectAllVisible() {
  Object.keys(checked).forEach((k) => {
    const [t, a] = k.split("||");
    // Agora o "all" é simplesmente o que está no SUGGESTIONS
    const all = SUGGESTIONS[t]?.[a] || [];
    all.forEach((i) => checked[k].add(i));
  });

  if (getKey()) {
    selectAllInPanel();
    return;
  }
  renderPanel();
}

function deselectAll() {
  checked = {};
  renderPanel();
}

// ═══════════════════════════════════════════════
// DRAG SELECT
// ═══════════════════════════════════════════════
function rowMouseDown(e, item) {
  if (e.button !== 0) return;
  const cs = getCheckedSet();
  dragMode = cs.has(item) ? "uncheck" : "check";
  toggleItem(item, dragMode === "check");
  isDragging = true;
  e.preventDefault();
}
function rowMouseEnter(e, item) {
  if (!isDragging) return;
  toggleItem(item, dragMode === "check");
}
function setupDragSelect() {
  document.addEventListener("mouseup", () => {
    isDragging = false;
    dragMode = null;
  });
  document.addEventListener("mouseleave", () => {
    isDragging = false;
    dragMode = null;
  });
}

// ═══════════════════════════════════════════════
// STACK
// ═══════════════════════════════════════════════
function addStackRow() {
  const t = document.getElementById("stkTech").value.trim();
  const v = document.getElementById("stkVer").value.trim();
  if (!t) return;
  stackRows.push({ tech: t, ver: v });
  document.getElementById("stkTech").value = "";
  document.getElementById("stkVer").value = "";
  renderStack();
  document.getElementById("stkTech").focus();
}
function delStackRow(i) {
  stackRows.splice(i, 1);
  renderStack();
}
function renderStack() {
  document.getElementById("stackBody").innerHTML = stackRows
    .map(
      (r, i) => `
    <tr>
      <td><input type="text" value="${escAttr(r.tech)}" oninput="stackRows[${i}].tech=this.value"></td>
      <td><input type="text" value="${escAttr(r.ver)}"  oninput="stackRows[${i}].ver=this.value" style="max-width:120px"></td>
      <td><button class="btn btn-sm" onclick="delStackRow(${i})" style="padding:2px 6px;color:var(--text3)">✕</button></td>
    </tr>`,
    )
    .join("");
}
function clearStack() {
  stackRows = [];
  renderStack();
}

// ═══════════════════════════════════════════════
// GENERATE
// ═══════════════════════════════════════════════
function collectMd() {
  const ver = document.getElementById("ver").value.trim() || "0.1.0";
  const dt =
    document.getElementById("dt").value.trim() ||
    new Date().toISOString().slice(0, 10);
  const name = document.getElementById("verName").value.trim();
  const header = name ? `## [${ver}] - ${dt} · ${name}` : `## [${ver}] - ${dt}`;

  const groups = {};
  let total = 0;
  Object.entries(checked).forEach(([k, cs]) => {
    if (cs.size === 0) return;
    const [tag, area] = k.split("||");
    if (!groups[tag]) groups[tag] = {};
    if (!groups[tag][area]) groups[tag][area] = [];
    cs.forEach((item) => {
      groups[tag][area].push(item);
      total++;
    });
  });

  let lines = [header];
  tags.forEach((tag) => {
    if (!groups[tag]) return;
    lines.push(`\n### ${tag}`);
    areas.forEach((area) => {
      if (!groups[tag][area]?.length) return;
      lines.push(`- **${area}**`);
      groups[tag][area].forEach((item) => lines.push(`  - ${item}`));
    });
  });

  if (stackRows.length > 0) {
    lines.push("\n### Stack Técnico");
    lines.push("| Tecnologia | Versão |");
    lines.push("| ---------- | ------ |");
    stackRows.forEach((r) => lines.push(`| ${r.tech} | ${r.ver} |`));
  }

  const today = new Date().toLocaleDateString("pt-BR", {
    day: "2-digit",
    month: "long",
    year: "numeric",
  });
  lines.push(`\n---\n**Última atualização:** ${today}`);

  return { md: lines.join("\n"), ver, dt, name, total };
}

function generate() {
  const { md, ver, total } = collectMd();
  document.getElementById("mdOut").value = md;
  document.getElementById("outMeta").textContent =
    `v${ver} · ${total} entrada${total !== 1 ? "s" : ""}`;
  document.getElementById("outputSection").style.display = "block";
  document
    .getElementById("outputSection")
    .scrollIntoView({ behavior: "smooth", block: "start" });
}

// ═══════════════════════════════════════════════
// SAVE VERSION
// ═══════════════════════════════════════════════
async function saveVersion() {
  const { md, ver, dt, name, total } = collectMd();
  if (total === 0) {
    alert("Selecione pelo menos um item antes de salvar.");
    return;
  }

  // Garante pasta selecionada
  if (!dirHandle) {
    const picked = await selectFolder();
    if (!picked) return;
  }
  if (!(await ensurePermission())) {
    alert("Sem permissão para acessar a pasta. Tente selecionar novamente.");
    dirHandle = null;
    updateDirUI();
    return;
  }

  const entry = {
    ver,
    dt,
    name,
    md,
    savedAt: new Date().toISOString(),
    filename: "",
  };
  const idx = versions.findIndex((v) => v.ver === ver);

  if (idx >= 0) {
    if (!confirm(`Versão ${ver} já existe. Substituir?`)) return;
    entry.filename = versions[idx].filename; // mantém o mesmo nome de arquivo
    versions[idx] = entry;
  } else {
    versions.unshift(entry); // nova: vai para o topo
  }

  try {
    const fname = await writeVersionFile(entry);
    entry.filename = fname;
    if (idx >= 0) versions[idx].filename = fname;
    else versions[0].filename = fname;

    await writeCumulativeChangelog();

    renderSidebarVersions();
    alert(
      `✓ Versão ${ver} salva em ${dirHandle.name}/\nArquivo individual: ${fname}\nCHANGELOG.md atualizado.`,
    );
  } catch (e) {
    // Reverte memória se falhou em disco
    if (idx >= 0)
      versions[idx] = versions[idx]; // já está lá
    else versions.shift();
    console.error("saveVersion:", e);
    alert("Erro ao salvar: " + e.message);
  }
}

// ═══════════════════════════════════════════════
// DELETE VERSION
// ═══════════════════════════════════════════════
async function deleteVersion(i) {
  if (!confirm(`Deletar versão ${versions[i].ver}?`)) return;
  const entry = versions[i];

  if (dirHandle && (await ensurePermission()) && entry.filename) {
    try {
      await dirHandle.removeEntry(entry.filename);
    } catch (e) {
      console.warn("Não foi possível remover o arquivo:", e);
    }
  }

  versions.splice(i, 1);

  try {
    await writeCumulativeChangelog();
  } catch {
    /* silent */
  }

  renderSidebarVersions();
  if (currentView === "history") renderHistory();
}

// ═══════════════════════════════════════════════
// HISTORY
// ═══════════════════════════════════════════════
function renderSidebarVersions() {
  const list = document.getElementById("sbVersionList");
  if (versions.length === 0) {
    list.innerHTML = `<div style="padding:4px 8px;font-size:.73rem;color:var(--text3)">${dirHandle ? "Nenhuma versão ainda" : "Conecte uma pasta para ver"}</div>`;
    return;
  }
  list.innerHTML = versions
    .map(
      (v, i) => `
    <div class="sb-ver-entry" onclick="loadVersion(${i})">
      <span>v${v.ver}${v.name ? " · " + escHtml(v.name) : ""}</span>
      <button class="sb-ver-del" onclick="event.stopPropagation();deleteVersion(${i})">✕</button>
    </div>`,
    )
    .join("");
}

function loadVersion(i) {
  const v = versions[i];
  document.getElementById("mdOut").value = v.md;
  document.getElementById("outMeta").textContent =
    `v${v.ver} — carregado do histórico`;
  document.getElementById("outputSection").style.display = "block";
  showView("editor");
}

function renderHistory() {
  const list = document.getElementById("historyList");
  const empty = document.getElementById("historyEmpty");
  if (versions.length === 0) {
    list.innerHTML = "";
    empty.style.display = "block";
    return;
  }
  empty.style.display = "none";
  list.innerHTML = versions
    .map(
      (v, i) => `
    <div class="history-card">
      <div class="history-card-header" onclick="toggleHCard('hb${i}')">
        <div>
          <div class="hv-title">v${v.ver}${v.name ? " — " + escHtml(v.name) : ""}</div>
          <div class="hv-meta">${v.dt} · salvo em ${new Date(v.savedAt).toLocaleString("pt-BR")}${v.filename ? " · " + escHtml(v.filename) : ""}</div>
        </div>
        <span style="font-size:.8rem;color:var(--text3)">▾</span>
      </div>
      <div class="history-card-body" id="hb${i}">
        <pre>${escHtml(v.md)}</pre>
        <div style="display:flex;gap:8px;margin-top:12px;flex-wrap:wrap">
          <button class="btn btn-sm" onclick="copyVer(${i})">Copiar</button>
          <button class="btn btn-sm" onclick="dlSingle(${i})">Baixar .md</button>
          <button class="btn btn-sm" style="color:var(--accent)" onclick="deleteVersion(${i})">Deletar</button>
        </div>
      </div>
    </div>`,
    )
    .join("");
}

function toggleHCard(id) {
  const el = document.getElementById(id);
  el.style.display = el.style.display === "block" ? "none" : "block";
}
function copyVer(i) {
  navigator.clipboard.writeText(versions[i].md).catch(() => {
    const ta = document.createElement("textarea");
    ta.value = versions[i].md;
    document.body.appendChild(ta);
    ta.select();
    document.execCommand("copy");
    document.body.removeChild(ta);
  });
}
function dlSingle(i) {
  const v = versions[i];
  dl(`CHANGELOG-${v.ver}.md`, v.md);
}

// ═══════════════════════════════════════════════
// EXPORT
// ═══════════════════════════════════════════════
async function exportAll() {
  if (versions.length === 0) {
    alert("Nenhuma versão salva.");
    return;
  }

  if (dirHandle && (await ensurePermission())) {
    // Escreve diretamente na pasta — nenhum download necessário
    await writeCumulativeChangelog();
    alert(
      `✓ CHANGELOG.md atualizado em ${dirHandle.name}/CHANGELOG.md\n\nEste arquivo já contém todas as versões salvas em ordem cronológica inversa.`,
    );
  } else {
    // Fallback: download se não tiver pasta
    const header =
      "# Histórico de Mudanças\n\n" +
      "Todas as mudanças notáveis serão documentadas neste arquivo.\n";
    dl("CHANGELOG.md", header + versions.map((v) => v.md).join("\n\n---\n\n"));
  }
}

function downloadCurrent() {
  const ver = document.getElementById("ver").value.trim() || "entry";
  dl(`CHANGELOG-${ver}.md`, document.getElementById("mdOut").value);
}

function copyOutput() {
  navigator.clipboard
    .writeText(document.getElementById("mdOut").value)
    .catch(() => {
      const ta = document.getElementById("mdOut");
      ta.select();
      document.execCommand("copy");
    });
}

function dl(filename, text) {
  const a = document.createElement("a");
  a.href = "data:text/markdown;charset=utf-8," + encodeURIComponent(text);
  a.download = filename;
  a.click();
}

// ═══════════════════════════════════════════════
// MISC
// ═══════════════════════════════════════════════
function clearForm() {
  checked = {};
  stackRows = [];
  document.getElementById("ver").value = "";
  document.getElementById("verName").value = "";
  document.getElementById("dt").value = new Date().toISOString().slice(0, 10);
  selTag = null;
  selArea = null;
  renderTagPills();
  renderAreaPills();
  renderPanel();
  renderStack();
  document.getElementById("outputSection").style.display = "none";
}

function escHtml(s) {
  return s.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}
function escAttr(s) {
  return s.replace(/"/g, "&quot;").replace(/'/g, "&#39;");
}

// ═══════════════════════════════════════════════
// BOOT
// ═══════════════════════════════════════════════
document.getElementById("dt").value = new Date().toISOString().slice(0, 10);
renderTagPills();
renderAreaPills();
renderPanel();
renderStack();
setupDragSelect();
initDir(); // async — carrega handle e versões do disco
