// Exportador de PDF do relatório completo de estoque.
// Mesma base do exportReportPDF (html2canvas + jsPDF via CDN), mas com PAGINAÇÃO:
// cada elemento marcado com data-pdf-block é renderizado e colocado na página;
// se não couber, vai para a próxima. Blocos maiores que uma página são fatiados.

const loadScript = (src) =>
  new Promise((resolve, reject) => {
    const s = document.createElement('script');
    s.src = src;
    s.onload = resolve;
    s.onerror = reject;
    document.head.appendChild(s);
  });

const slugify = (text) =>
  String(text || 'restaurante')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[^a-zA-Z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '')
    .toLowerCase();

export const exportStockReportPDF = async ({ container, restaurantName, startDate, endDate }) => {
  if (!container) throw new Error('Relatório não encontrado na tela.');

  if (!globalThis.html2canvas) {
    await loadScript('https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js');
  }
  if (!globalThis.jspdf) {
    await loadScript('https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js');
  }

  const blocks = Array.from(container.querySelectorAll('[data-pdf-block]'));
  if (blocks.length === 0) throw new Error('Nada para exportar.');

  const { jsPDF } = globalThis.jspdf;
  const pdf = new jsPDF({ orientation: 'portrait', unit: 'px', format: 'a4' });
  const pageW = pdf.internal.pageSize.getWidth();
  const pageH = pdf.internal.pageSize.getHeight();
  const margin = 24;
  const gap = 8;
  const contentW = pageW - margin * 2;
  const contentH = pageH - margin * 2;
  let y = margin;

  for (const block of blocks) {
    // eslint-disable-next-line no-await-in-loop
    const canvas = await globalThis.html2canvas(block, {
      scale: 2,
      useCORS: true,
      backgroundColor: '#ffffff',
    });

    const ratio = contentW / canvas.width;
    const blockH = canvas.height * ratio;

    if (blockH <= contentH) {
      // cabe numa página: se não couber no espaço que sobrou, abre página nova
      if (y + blockH > pageH - margin) {
        pdf.addPage();
        y = margin;
      }
      pdf.addImage(canvas.toDataURL('image/png'), 'PNG', margin, y, contentW, blockH, undefined, 'FAST');
      y += blockH + gap;
    } else {
      // maior que uma página: fatia em pedaços
      if (y > margin) {
        pdf.addPage();
      }
      const sliceHeightPx = Math.floor(contentH / ratio);
      for (let offset = 0; offset < canvas.height; offset += sliceHeightPx) {
        const h = Math.min(sliceHeightPx, canvas.height - offset);
        const slice = document.createElement('canvas');
        slice.width = canvas.width;
        slice.height = h;
        slice.getContext('2d').drawImage(canvas, 0, offset, canvas.width, h, 0, 0, canvas.width, h);
        if (offset > 0) pdf.addPage();
        pdf.addImage(slice.toDataURL('image/png'), 'PNG', margin, margin, contentW, h * ratio, undefined, 'FAST');
      }
      y = pageH; // força página nova para o próximo bloco
    }
  }

  // rodapé com numeração
  const totalPages = pdf.getNumberOfPages();
  for (let i = 1; i <= totalPages; i += 1) {
    pdf.setPage(i);
    pdf.setFontSize(8);
    pdf.setTextColor(150);
    pdf.text(`Página ${i} de ${totalPages}`, pageW - margin, pageH - 10, { align: 'right' });
  }

  pdf.save(`relatorio-completo-${slugify(restaurantName)}-${startDate}-${endDate}.pdf`);
};
