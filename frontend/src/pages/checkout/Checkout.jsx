import { isValidPhoneNumber, parsePhoneNumberFromString, validatePhoneNumberLength } from 'libphonenumber-js';
import postalCodes from 'postal-codes-js';
import { useEffect, useState } from 'react';
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css';
import { useNavigate } from "react-router-dom";
import { CheckoutOrderDetails, CheckoutShippingInfo, FetchShippingInfo } from '../../components/API/CheckoutApi';
import './Checkout.css';

function CheckoutPage() {
    const navigate = useNavigate();
    const [products] = useState([
        { id: 1, name: 'Product 1', price: 100 },
        { id: 2, name: 'Product 2', price: 200 },
    ]);
    const [phone, setPhone] = useState('');
    const [address, setAddress] = useState('');
    const [postalCode, setPostalCode] = useState('');
    const [phoneCountry, setPhoneCountry] = useState('us');
    const [isVisible_1, setIsVisible_1] = useState(false);
    const [isVisible_2, setIsVisible_2] = useState(false);
    const [isVisible_3, setIsVisible_3] = useState(false);

    useEffect(() => {
        const hasVisitedRadialChoice = localStorage.getItem(
            "hasVisitedRadialChoice"
        );
        if (!hasVisitedRadialChoice) {
            navigate("/");
        }
    }, [navigate]);

    useEffect(() => {
        console.log(sessionStorage.getItem("id"));
        const fetchShippingInfo = async () => {
            const response = await FetchShippingInfo(sessionStorage.getItem("id"));
            if (response) {
                setAddress(response.address || '');
                setPhone(response.phoneNumber || '');
                setPostalCode(response.postalCode || '');
            }
        };
        fetchShippingInfo();
    }, []);

    function handleAddress(event) {
        const val = event.target.value;
        setAddress(val);
        const messageContainer_1 = document.getElementById("messageContainer-1");
        if (val.length === 0) {
            messageContainer_1.textContent = "The address can't be empty";
            setIsVisible_1(true);
        } else {
            messageContainer_1.textContent = "";
            setIsVisible_1(false);
        }
    }

    function handlePostalCode(event) {
        const val = event.target.value;
        setPostalCode(val);
        const messageContainer_2 = document.getElementById("messageContainer-2");

        if (val.length === 0) {
            messageContainer_2.textContent = "The postal code can't be empty";
            setIsVisible_2(true);
        } else {
            messageContainer_2.textContent = "";
            setIsVisible_2(false);
        }
    }

    function handlePhone(value) {
        const messageContainer_3 = document.getElementById("messageContainer-3");
        if (value.length === 0) {
            messageContainer_3.textContent = "The phone number can't be empty";
            setIsVisible_3(true);
        } else {
            messageContainer_3.textContent = "";
            setIsVisible_3(false);
        }
    }

    const PerformCheckout = async (event) => {
        event.preventDefault();

        const messageContainer_3 = document.getElementById("messageContainer-3");
        const phoneNumber = parsePhoneNumberFromString(`+${phone}`, phoneCountry.toUpperCase());
        if (!phoneNumber || !phoneNumber.isValid()) {
            messageContainer_3.textContent = "InValid Phone Number";
            setIsVisible_3(true);
            return;
        }
        if (!isValidPhoneNumber(`+${phone}`)) {
            messageContainer_3.textContent = "InCorrect Phone Number";
            setIsVisible_3(true);
            return;
        }

        if(!validatePhoneNumberLength(`+${phone}`,phoneCountry)){
            messageContainer_3.textContent = "InComplete Phone Number";
            setIsVisible_3(true);
            return;
        }

        const messageContainer_2 = document.getElementById("messageContainer-2");
        if (phoneCountry && postalCodes.validate(phoneCountry, postalCode) !== true) {
            messageContainer_2.textContent = `Postal code is invalid for the selected country (${phoneCountry.toUpperCase()})`;
            setIsVisible_2(true);
            return;
        }
        if (!isVisible_1 && !isVisible_2 && !isVisible_3) {
            const total_price = document.getElementById("total-price").value;
            console.log("ready to submit");
            console.log(address);
            console.log(phone);
            console.log(postalCode);
            console.log(total_price);
            console.log(sessionStorage.getItem("id"))
            const response1 = await CheckoutShippingInfo(sessionStorage.getItem("id"), address, phone, postalCode);
            console.log(response1);

            const response2 = await CheckoutOrderDetails(1, total_price, products);
            console.log(response2);
            if (response2.startsWith("Order is Registered Successfully") && (response1 === "Shipping Info Updated Successfully" || response1 === "Shipping Info Inserted Successfully")) {
                sessionStorage.setItem('order_number', response2.split(",")[1]);
                sessionStorage.setItem('total', total_price);
                navigate("/checkout/complete");
            }
        }
    }

    return (
        <div className="checkout-container">
            <form className="checkout-form" onSubmit={PerformCheckout}>
                <h2 className="checkout-title">Checkout Form</h2>

                <div className="checkout-form-group">
                    <label htmlFor="address" className="checkout-label">Address</label>
                    <input
                        type="text"
                        id="address"
                        name="address"
                        placeholder="Enter your address"
                        className="checkout-form-input"
                        value={address}
                        onChange={handleAddress}
                        required
                    />
                    <div
                        id="messageContainer-1"
                        className={isVisible_1 ? "checkout-visible" : "checkout-hidden"}
                    ></div>
                </div>

                <div className="checkout-form-group">
                    <label htmlFor="phone" className="checkout-label">Phone Number</label>
                    <PhoneInput className="checkout-phone-input"
                        country={phoneCountry}
                        value={phone}
                        onChange={(value, country) => {
                            setPhone(value);
                            setPhoneCountry(country.countryCode);
                            handlePhone(value);
                        }}
                        required
                        inputStyle={{
                            width: '100%',
                            padding: '10px',
                            borderRadius: '4px',
                            border: '1px solid #ccc',
                            fontSize: 'medium',
                        }}
                        dropdownStyle={{
                            fontSize: '16px',
                        }}
                    />
                    <div
                        id="messageContainer-3"
                        className={isVisible_3 ? "checkout-visible" : "checkout-hidden"}
                    ></div>
                </div>

                <div className="checkout-form-group">
                    <label htmlFor="postal-code" className="checkout-label">Postal Code</label>
                    <input
                        type="text"
                        id="postal-code"
                        name="postalCode"
                        placeholder="Enter your postal code"
                        className="checkout-form-input"
                        value={postalCode}
                        onChange={handlePostalCode}
                        required
                    />
                    <div
                        id="messageContainer-2"
                        className={isVisible_2 ? "checkout-visible" : "checkout-hidden"}
                    ></div>
                </div>

                <div className="checkout-form-group">
                    <label htmlFor="total-price" className="checkout-label">Total Price</label>
                    <input
                        type="text"
                        id="total-price"
                        name="totalPrice"
                        value={products.reduce((total, product) => total + product.price, 0)}
                        readOnly
                        className="checkout-form-input read-only"
                    />
                </div>

                <h3 className="checkout-products-title">Products</h3>
                <div className="checkout-products-list">
                    {products.map((product, index) => (
                        <div key={index} className="checkout-product-box">
                            <span>{product.name}</span>
                            <span>${product.price}</span>
                        </div>
                    ))}
                </div>

                <button type="submit" className="checkout-submit-button">Submit</button>
            </form>
        </div>
    );
};

export default CheckoutPage;
