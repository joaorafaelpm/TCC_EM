
class CommomApi {
  static getAllAndFindCreatedObject(accessToken, createdCityName) {
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

  static getById(accessToken, cityId , endpoint) {
    return cy.request({
      method: "GET",
      url: `${endpoint}/${cityId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

}
export default CommomApi;
