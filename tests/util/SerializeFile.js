const fs = require("fs");
const ser = require("serialize-anything");
const { fileExists } = require("./fileExists");
const { ERROR } = require("./ERROR");

const changelogPath = "cypress/.cache";
function serialize(info, filename) {
  let jstr = ser.serialize(info);
  removeChangelogSerializedFileIfExist();
  fs.mkdirSync(changelogPath, { recursive: true });
  fs.writeFileSync(`${changelogPath}/${filename}.obj`, jstr);
}

function unSerialize(filename) {
  const path = `${changelogPath}/${filename}.obj`;

  if (!fileExists(path)) {
    ERROR(
      "Não é possível continuar a execução da pipeline. Verifique se o job anterior rodou noemalmente.",
    );
  }

  let infoBuffer = fs.readFileSync(path);
  let info = ser.deserialize(infoBuffer);

  return info;
}

/**
 * Remove pasta com arquivos de rollback se existir
 *
 */
function removeChangelogSerializedFileIfExist() {
  if (fs.existsSync(changelogPath)) {
    fs.rmSync(changelogPath, { recursive: true, force: true });
  }
}

module.exports = {
  serialize,
  unSerialize,
};
