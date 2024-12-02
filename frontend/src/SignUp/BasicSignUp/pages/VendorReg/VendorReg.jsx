import { useEffect, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import backgroundImg from '../../../assets/background.png';
import VendorBasicSignUp from '../component/API/VendorApi.jsx';
import './VendorReg.css';

function VendorReg() {
    const navigate = useNavigate();

    const [passwordVisible, setPasswordVisible] = useState(false);
    const [businessname, setBusinessname] = useState("");
    const [password, setPassword] = useState("");
    const [taxnumber, setTaxnumber] = useState("");

    const [isVisible_1, setIsVisible_1] = useState(false);
    const [isVisible_2, setIsVisible_2] = useState(false);
    const [isVisible_3, setIsVisible_3] = useState(false);

    useEffect(() => {
        const hasVisitedRadialChoice = localStorage.getItem("hasVisitedRadialChoice");
        if (!hasVisitedRadialChoice) {
            navigate("/");
        }
    }, [navigate]);


    function switchVisibility() {
        setPasswordVisible(!passwordVisible)
    }

    function handleBusinessnameChange(event) {
        const val = event.target.value;
        setBusinessname(val);
        const messageContainer_1 = document.getElementById("messageContainer-1");
        if (val.length > 80) {
            messageContainer_1.textContent = "The business name can't be more than 80 character";
            setIsVisible_1(true);
        }
        else if(val.length == 0){
            messageContainer_1.textContent = "The business name can't be empty";
            setIsVisible_1(true);
        }
        else {
            messageContainer_1.textContent = "";
            setIsVisible_1(false);
        }
    }

    function handleTaxnumberChange(event) {
        const val = event.target.value;
        setTaxnumber(val);
        const messageContainer_2 = document.getElementById("messageContainer-2");
        if (val.length == 0) {
            messageContainer_2.textContent = "The tax number can't be empty";
            setIsVisible_2(true);
        }
        else if (val.length != 9) {
            messageContainer_2.textContent = "The tax number must be of 9 numbers only";
            setIsVisible_2(true);
        }
        else {
            messageContainer_2.textContent = "";
            setIsVisible_2(false);
        }
    }

    function handlePasswordChange(event) {
        const val = event.target.value;
        setPassword(val);
        const messageContainer_3 = document.getElementById("messageContainer-3");
        if (val.length > 80) {
            messageContainer_3.textContent = "The password can't be more than 80 character";
            setIsVisible_3(true);
        }
        else if (val.length == 0) {
            messageContainer_3.textContent = "The password can't be empty";
            setIsVisible_3(true);
        }
        else {
            messageContainer_3.textContent = "";
            setIsVisible_3(false);
        }
    }

    const Register = async (event) =>{
        event.preventDefault();
        var businessname = document.getElementById("businessname").value;
        var taxnumber = document.getElementById("taxnumber").value;
        var password = document.getElementById("password").value;
        console.log(businessname);
        console.log(taxnumber);
        console.log(password);
        if (!isVisible_1 && !isVisible_2 && !isVisible_3) {
            const response = await VendorBasicSignUp(businessname, password, taxnumber);
            console.log(response);

            if (response == "Successfully registered") {
                console("response");
            }
            else if (response.includes("business name")) {
                const messageContainer_1 = document.getElementById("messageContainer-1");
                messageContainer_1.textContent = response;
                setIsVisible_1(true);
            }
            else if (response.includes("tax number")) {
                const messageContainer_2 = document.getElementById("messageContainer-2");
                messageContainer_2.textContent = response;
                setIsVisible_2(true);
            }
            else if (response.includes("password")) {
                const messageContainer_3 = document.getElementById("messageContainer-3");
                messageContainer_3.textContent = response;
                setIsVisible_3(true);
            }
        }
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
                        value={businessname}
                        onChange={handleBusinessnameChange}
                        required
                    />
                    <div id="messageContainer-1" className={isVisible_1 ? 'visible' : 'hidden'}></div>
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
                        value={taxnumber}
                        onChange={handleTaxnumberChange}
                        required
                    />
                    <div id="messageContainer-2" className={isVisible_2 ? 'visible' : 'hidden'}></div>
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
                            value={password}
                            onChange={handlePasswordChange}
                            required
                        />
                        <span className="eye-icon" onClick={() => switchVisibility()}>
                            {passwordVisible ? <FaEye /> : <FaEyeSlash />}
                        </span>
                    </div>
                    <div id="messageContainer-3" className={isVisible_3 ? 'visible' : 'hidden'}></div>
                </div>
                <button type="submit" className="form-button">
                    Submit
                </button>
            </form>
        </div>
    );
};

export default VendorReg;