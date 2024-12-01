import React, { useEffect, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import backgroundImg from '../../../assets/background.png';
import VendorBasicSignUp from '../API/VendorApi.jsx';
import './VendorReg.css';

function VendorReg() {
    const navigate = useNavigate();

    const [passwordVisible, setPasswordVisible] = useState(false);
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const hasVisitedRadialChoice = localStorage.getItem("hasVisitedRadialChoice");
        if (!hasVisitedRadialChoice) {
            navigate("/");
        }
    }, [navigate]);


    function switchVisibility() {
        setPasswordVisible(!passwordVisible)
    }

    function Register(event) {
        event.preventDefault();
        var businessname = document.getElementById("businessname").value;
        var taxnumber = document.getElementById("taxnumber").value;
        var password = document.getElementById("password").value;
        console.log(businessname);
        console.log(taxnumber);
        console.log(password);
        VendorBasicSignUp(businessname, password, taxnumber);
        // data = "hello";
        // console.log(data);
        // const messageContainer = document.getElementById("messageContainer");
        // messageContainer.textContent = data;
        // setIsVisible(!isVisible);
    };

    return (
        <div className="page">
            <img src={backgroundImg} className="background" />
            <form className="form-new" onSubmit={Register}>
                <h2 className="form-header">Vendor Sign Up</h2>
                <div className="group">
                    <label className="form-label">
                        Buisness Name
                    </label>
                    <input className="form-input"
                        placeholder="Enter your business name"
                        type="text"
                        id="businessname"
                        name="businessname"
                        required
                    />
                    {/* <div id="messageContainer" className={isVisible ? 'visible' : 'hidden'}></div> */}
                </div>
                <div className="group">
                    <label className="form-label">
                        Tax Number
                    </label>
                    <input className="form-input"
                        placeholder="Enter your tax number"
                        type="text"
                        id="taxnumber"
                        name="taxnumber"
                        required
                    />
                    {/* <div id="messageContainer" className={isVisible ? 'visible' : 'hidden'}></div> */}
                </div>
                <div className="group">
                    <label className="form-label" htmlFor="password">
                        Password
                    </label>
                    <div className="form-password-container">
                        <input className="form-input"
                            type={passwordVisible ? "text" : "password"}
                            placeholder="Enter your password"
                            id="password"
                            name="password"
                            required
                        />
                        <span className="eye-icon" onClick={() => switchVisibility()}>
                            {passwordVisible ? <FaEye /> : <FaEyeSlash />}
                        </span>
                    </div>
                    {/* <div id="messageContainer" className={isVisible ? 'visible' : 'hidden'}></div> */}
                </div>
                <button type="submit" className="form-button">
                    Submit
                </button>
            </form>
        </div>
    );
};

export default VendorReg;