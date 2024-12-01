function VendorBasicSignUp(businessname, password, taxnumber) {
    return fetch('http://localhost:8080/SignUp/VendorBasicSignUp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            businessname: businessname,
            password: password,
            taxnumber: taxnumber,
        }),
    })
        .then(response => response.text())
        .then(data => {
            console.log("Response from the backend:", data);
            return data;
        })
}

export default VendorBasicSignUp;