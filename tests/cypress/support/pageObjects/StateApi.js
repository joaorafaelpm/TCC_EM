const stateURL = "/v1/states";

class StateApi {
  static createState(accessToken) {
    return cy.request({
      method: "POST",
      url: stateURL,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: {
        name: "São Paulo",
      },
      failOnStatusCode: false,
    });
  }

  static createStateWithCustomName(name, accessToken) {
    return cy.request({
      method: "POST",
      url: stateURL,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: {
        name: name,
      },
      failOnStatusCode: false,
    });
  }

  static getState(accessToken, createdStateName) {
    return cy
      .request({
        method: "GET",
        url: stateURL,
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        const state = response.body._embedded.states.find(
          (item) => item.name === createdStateName,
        );
        return state;
      });
  }

  static getStateById(accessToken, stateId) {
    return cy.request({
      method: "GET",
      url: `${stateURL}/${stateId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getStateByIdWithAcceptHeader(accessToken, stateId, acceptHeader) {
    return cy.request({
      method: "GET",
      url: `${stateURL}/${stateId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: acceptHeader,
      },
      failOnStatusCode: false,
    });
  }

  static deleteState(accessToken, stateId) {
    return cy.request({
      method: "DELETE",
      url: `${stateURL}/${stateId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static updateState(accessToken, stateId, body) {
    return cy.request({
      method: "PUT",
      url: `${stateURL}/${stateId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }
}

export default StateApi;

