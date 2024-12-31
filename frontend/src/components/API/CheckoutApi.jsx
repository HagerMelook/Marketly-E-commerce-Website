export function CheckoutShippingInfo(account_id, address, phone_number, postal_code) {
    return fetch("http://localhost:8080/checkout/shippingInfo", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            // "Authorization": `Bearer ${sessionStorage.getItem('token')}`,
        },
        body: JSON.stringify({
            id: account_id,
            address: address,
            phoneNumber: phone_number,
            postalCode: postal_code
        }),
    })
        .then((response) => response.text())
        .then((data) => {
            console.log("Response from the backend:", data);
            return data;
        })
        .catch((error) => {
            console.error(error);
            throw error;
        });
}

export function CheckoutOrderDetails(account_id, totalPrice, products) {
    return fetch("http://localhost:8080/checkout/orderDetails", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            // "Authorization": `Bearer ${sessionStorage.getItem('token')}`,
        },
        body: JSON.stringify({
            account_id: account_id,
            checkoutPrice: totalPrice,
            order_products: products
        }),
    })
        .then((response) => response.text())
        .then((data) => {
            console.log("Response from the backend:", data);
            return data;
        })
        .catch((error) => {
            console.error(error);
            throw error;
        });
}

export function FetchShippingInfo(account_id) {
    return fetch(`http://localhost:8080/checkout/${account_id}`, {
        method: 'GET',
        // headers: {
        //     "Authorization": `Bearer ${sessionStorage.getItem('token')}`,
        // },
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Failed to fetch shipping info');
            }
            return response.json();
        })
        .then((data) => {
            console.log("Response from the backend:", data);
            return data;
        })
        .catch((error) => {
            console.error(error);
            throw error; 
        });
}

