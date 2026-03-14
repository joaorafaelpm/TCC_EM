const userUrl = "/v1/users";

class UserApi {
  static createUser(accessToken, body) {
    return cy.request({
      method: "POST",
      url: userUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }


  static createUserWithCustomBody(accessToken, body) {
    return cy.request({
      method: "POST",
      url: userUrl,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: body,
      failOnStatusCode: false,
    });
  }

  static getUser(accessToken, createdEmail) {
    return cy
      .request({
        method: "GET",
        url: userUrl,
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        const user = response.body._embedded.users.find(
          (item) => item.email === createdEmail,
        );
        return user;
      });
  }

  static getUserById(accessToken, userId) {
    return cy.request({
      method: "GET",
      url: `${userUrl}/${userId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getUserByIdWithAcceptHeader(accessToken, userId, acceptHeader) {
    return cy.request({
      method: "GET",
      url: `${userUrl}/${userId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        Accept: acceptHeader,
      },
      failOnStatusCode: false,
    });
  }

  static updateUser(accessToken, userId, body) {
    return cy.request({
      method: "PUT",
      url: `${userUrl}/${userId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      body: body,
      failOnStatusCode: false,
    });
  }

  static updateUserPassword(accessToken, userId, body) {
    return cy.request({
      method: "PUT",
      url: `${userUrl}/${userId}/password`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      body: body,
      failOnStatusCode: false,
    });
  }

  static deleteUser(accessToken, userId) {
    return cy.request({
      method: "DELETE",
      url: `${userUrl}/${userId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static getGroupsByUserId(accessToken, userId) {
    return cy.request({
      method: "GET",
      url: `${userUrl}/${userId}/groups`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }

  static userAndGroupMethod(accessToken, method, userId, groupId) {
    return cy.request({
      method: method,
      url: `${userUrl}/${userId}/groups/${groupId}`,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      failOnStatusCode: false,
    });
  }
}

export default UserApi;
