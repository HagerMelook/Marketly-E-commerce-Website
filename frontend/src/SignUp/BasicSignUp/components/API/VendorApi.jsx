function VendorBasicSignUp(businessname, password, taxnumber) {
    if (taxnumber==null) taxnumber = -1;
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
        .catch((error)=>{
            console.error(error);
            throw error;
        })
}

export default VendorBasicSignUp;