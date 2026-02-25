
const statisticsUrl = "/v1/statistics/daily-sales";

class StatisticsApi {
  static getStatistics(accessToken) {
    return cy.request({
      method: "GET",
      url: statisticsUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
    });
  }
  static getStatisticsWithCustomId(accessToken , restaurantId) {
    return cy.request({
      method: "GET",
      url: (statisticsUrl + "?restaurantId=" + restaurantId) ,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      
    });
  }
  static getStatisticsPdf(accessToken) {
    return cy.request({
      method: "GET",
      url: statisticsUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/pdf",
      },
    });
  }
  static getStatisticsByIdWithAcceptHeader(accessToken, acceptHeader) {
    return cy.request({
      method: "GET",
      url: statisticsUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: acceptHeader,
      },
      failOnStatusCode: false,
    });
  }
}

export default StatisticsApi;