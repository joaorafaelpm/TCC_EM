
const permissionUrl = "/v1/permissions";

class PermissionApi {
  static getPermission(accessToken) {
    return cy.request({
      method: "GET",
      url: permissionUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
    });
  }
  static getPermissionByIdWithAcceptHeader(accessToken, acceptHeader) {
    return cy.request({
      method: "GET",
      url: permissionUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Accept": "application/pdf",
      },
      failOnStatusCode: false,
    });
  }
}

export default PermissionApi;

