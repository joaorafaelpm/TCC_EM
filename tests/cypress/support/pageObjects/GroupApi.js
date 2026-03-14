const groupUrl = "/v1/groups";

class GroupApi {
  static createGroup(accessToken) {
    return cy.request({
      method: "POST",
      url: groupUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: {
        name: "Cafetão",
      },
      failOnStatusCode: false,
    });
  }

  static createGroupWithCustomName(name, accessToken) {
    return cy.request({
      method: "POST",
      url: groupUrl,
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

  static getGroup(accessToken, createdGroupName) {
    return cy
      .request({
        method: "GET",
        url: groupUrl,
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        const group = response.body._embedded.groups.find(
          (item) => item.name === createdGroupName,
        );
        return group;
      });
  }

  static getGroupById(accessToken, groupId) {
    return cy.request({
      method: "GET",
      url: `${groupUrl}/${groupId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getPermissionsByGroupId(accessToken, groupId) {
    return cy.request({
      method: "GET",
      url: `${groupUrl}/${groupId}/permissions`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getGroupByIdWithAcceptHeader(accessToken, groupId, acceptHeader) {
    return cy.request({
      method: "GET",
      url: `${groupUrl}/${groupId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: acceptHeader,
      },
      failOnStatusCode: false,
    });
  }

  static deleteGroup(accessToken, groupId) {
    return cy.request({
      method: "DELETE",
      url: `${groupUrl}/${groupId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static updateGroup(accessToken, groupId, body) {
    return cy.request({
      method: "PUT",
      url: `${groupUrl}/${groupId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      body: body,
      failOnStatusCode: false,
    });
  }

  static groupAndPermissionMethod(accessToken, method, groupId, permissionId) {
    return cy.request({
      method: method,
      url: `${groupUrl}/${groupId}/permissions/${permissionId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }
}

export default GroupApi;
