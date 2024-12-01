function ClientBasicSignUp(username, password) {
    return fetch('http://localhost:8080/SignUp/ClientBasicSignUp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password,
        }),
    })
        .then(response => response.text())
        .then(data => {
            console.log("Response from the backend:", data);
            return data;
        })
}

export default ClientBasicSignUp;