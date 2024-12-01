import React, { useEffect, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import ClientBasicSignUp from '../API/ClientApi.jsx';
import backgroundImg from '../assets/background.png';
import './ClientReg.css';

function ClientReg() {
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
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;
        console.log(username);
        console.log(password);
        ClientBasicSignUp(username, password);
        // data = "hello"
        // console.log(data);
        // const messageContainer = document.getElementById("messageContainer");
        // messageContainer.textContent = data;
        // setIsVisible(!isVisible);
    };

    return (
        <div className="page">
            <img src={backgroundImg} className="background" />
            <form className="form-new" onSubmit={Register}>
                <h2 className="form-header">Client Sign Up</h2>
                <div className="group">
                    <label className="form-label">
                        Username
                    </label>
                    <input className="form-input"
                        placeholder="Enter your username"
                        type="text"
                        id="username"
                        name="username"
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

export default ClientReg;