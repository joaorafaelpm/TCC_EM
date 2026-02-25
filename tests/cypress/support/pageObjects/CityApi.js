const cityURL = "/v1/cities";

class CityApi {
  static createCity(stateId, accessToken) {
    return cy.request({
      method: "POST",
      url: cityURL,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: {
        name: "São Carlos",
        stateId: { id: stateId },
      },
      failOnStatusCode: false,
    });
  }

  static createCityWithCustomName(name , stateId, accessToken) {
    return cy.request({
      method: "POST",
      url: cityURL,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: {
        name: " ",
        stateId: { id: stateId },
      },
      failOnStatusCode: false,
    });
  }

  static getCity(accessToken, createdCityName) {
    return cy
      .request({
        method: "GET",
        url: cityURL,
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        const city = response.body._embedded.cities.find(
          (item) => item.name === createdCityName,
        );
        return city;
      });
  }

  static getCityById(accessToken, cityId) {
    return cy.request({
      method: "GET",
      url: `${cityURL}/${cityId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getCityByIdWithAcceptHeader(accessToken, cityId, acceptHeader) {
    return cy.request({
      method: "GET",
      url: `${cityURL}/${cityId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: acceptHeader,
      },
      failOnStatusCode: false,
    });
  }
  static deleteCity(accessToken, cityId) {
    return cy.request({
      method: "DELETE",
      url: `${cityURL}/${cityId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }
  static updateCity(accessToken, cityId, body) {
    return cy.request({
      method: "PUT",
      url: `${cityURL}/${cityId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }
}
export default CityApi;

