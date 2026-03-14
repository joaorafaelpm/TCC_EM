const { defineConfig } = require("cypress");
const dotenv = require("dotenv");
dotenv.config();
const fs = require("fs");
const path = require("path");
const {
  addCucumberPreprocessorPlugin,
} = require("@badeball/cypress-cucumber-preprocessor");
const {
  createEsbuildPlugin,
} = require("@badeball/cypress-cucumber-preprocessor/esbuild");
const createBundler = require("@bahmutov/cypress-esbuild-preprocessor");

const { serialize , unSerialize } = require("./util/SerializeFile");

function getConfigurationByFile(file) {
  const pathToConfigFile = path.resolve("cypress/config", `${file}.json`);

  if (!fs.existsSync(pathToConfigFile)) {
    console.log("No custom config file found.");
    return {};
  }

  return fs.readJson(pathToConfigFile);
}

module.exports = defineConfig({
  e2e: {
    async setupNodeEvents(on, config) {
    await addCucumberPreprocessorPlugin(on, config);

    const bundler = createBundler({
      plugins: [createEsbuildPlugin(config)],
    });

    on("file:preprocessor", bundler);

    on("task", {
      serializeData({ token, fileName }) {
        serialize(token, fileName);
        return null;
      },
      unSerializeData(fileName) {
        try {
          return unSerialize(fileName);
        } catch (error) {
          return null;
        }
      },
    });

    const file = config.env.configFile || "";
    const configJson = await getConfigurationByFile(file);

    return { 
      ...config, 
      ...configJson,
      env: {
        ...config.env,
        ...configJson.env
      }
    };
  },
  testIsolation: false,
  specPattern: "cypress/e2e/**/*.feature",
  chromeWebSecurity: false,
  projectId: "1vvroh",
  defaultCommandTimeout: 10000,
  pageLoadTimeout: 120000,
  screenshotOnRunFailure: true,
  trashAssetsBeforeRuns: true,
  failOnStatusCode: false,
  video: true,
  baseUrl:"http://localhost:8080",
  reporter: "cypress-multi-reporters",
  reporterOptions: {
    configFile: "reporter-config.json",
  },
  retries: {
    runMode: 0,
    openMode: 1,
  },
  env: {
    CLIENT_ID: process.env.CLIENT_ID,
    CLIENT_SECRET: process.env.CLIENT_SECRET,
    SCOPES: process.env.SCOPES,
    API_AUTH_URL: process.env.ACCESS_TOKEN_URL,
    API_URL: process.env.API_URL,
    REDIRECT_URL: process.env.REDIRECT_URL,
    CODE_VERIFIER: process.env.CODE_VERIFIER,
    CLIENT_USERNAME: process.env.CLIENT_USERNAME,
    CLIENT_PASSWORD: process.env.CLIENT_PASSWORD,
  },
},
});
