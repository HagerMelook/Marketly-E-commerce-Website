import { useState } from "react";
import { useNavigate } from "react-router-dom";
import backgroundImg from '../../../../assets/background.png';
import './RadialChoice.css';

function RadialChoice() {
    const [showPopUp, closePopUp] = useState(true);
    const navigate = useNavigate();

    function DisappearPopUp(choice) {
        // to prevent the user from go direst to the signup page
        localStorage.setItem("hasVisitedRadialChoice", true);

        closePopUp(false);
        if (choice == "Client")
            navigate("/clientSignUp");
        if (choice == "Vendor")
            navigate("/vendorSignUp");
    };

    return (
        <div>
            <img src={backgroundImg} className="background" />
            <div>
                {showPopUp && (
                    <div className="overlay">
                        <div className="text-overlay">
                            <h1 className="headline">Welcome to Our Site!</h1>
                        </div>
                        <div className="popup-card">
                            <h2 className="card-header">Choose Your Role</h2>
                            <button className="card-button" onClick={() => DisappearPopUp("Client")}>
                                Client
                            </button>
                            <button className="card-button" onClick={() => DisappearPopUp("Vendor")}>
                                Vendor
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default RadialChoice;