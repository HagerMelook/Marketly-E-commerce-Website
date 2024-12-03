import { useEffect, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import backgroundImg from "../../assets/background.png";
import ClientBasicSignUp from "../../components/API/ClientApi.jsx";
// import GoogleButton from "../../components/GoogleSignButton/GoogleSignButton.jsx";

import "./ClientReg.css";

function ClientReg() {
  const navigate = useNavigate();

  const [passwordVisible, setPasswordVisible] = useState(false);
  const [isVisible_1, setIsVisible_1] = useState(false);
  const [isVisible_2, setIsVisible_2] = useState(false);
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");

  useEffect(() => {
    const hasVisitedRadialChoice = localStorage.getItem(
      "hasVisitedRadialChoice"
    );
    if (!hasVisitedRadialChoice) {
      navigate("/");
    }
  }, [navigate]);

  function switchVisibility() {
    setPasswordVisible(!passwordVisible);
  }

  function handleUsernameChange(event) {
    const val = event.target.value;
    setUsername(val);
    const messageContainer_1 = document.getElementById("messageContainer-1");
    if (val.length > 80) {
      messageContainer_1.textContent =
        "The username can't be more than 80 character";
      setIsVisible_1(true);
    } else if (val.length == 0) {
      messageContainer_1.textContent = "The username can't be empty";
      setIsVisible_1(true);
    } else {
      messageContainer_1.textContent = "";
      setIsVisible_1(false);
    }
  }

  function handlePasswordChange(event) {
    const val = event.target.value;
    setPassword(val);
    const messageContainer_2 = document.getElementById("messageContainer-2");
    if (val.length > 80) {
      messageContainer_2.textContent =
        "The password can't be more than 80 character";
      setIsVisible_2(true);
    }
    if (val.length == 0) {
      messageContainer_2.textContent = "The password can't be empty";
      setIsVisible_2(true);
    } else {
      messageContainer_2.textContent = "";
      setIsVisible_2(false);
    }
  }

  const Register = async (event) => {
    console.log("Registering");
    event.preventDefault();
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    console.log(username);
    console.log(password);
    if (!isVisible_1 && !isVisible_2) {
      const response = await ClientBasicSignUp(username, password);
      console.log(response);

      if (response == "Successfully registered") {
        console.log("response");
      } else if (response.includes("username")) {
        const messageContainer_1 =
          document.getElementById("messageContainer-1");
        messageContainer_1.textContent = response;
        setIsVisible_1(true);
      } else if (response.includes("password")) {
        const messageContainer_2 =
          document.getElementById("messageContainer-2");
        messageContainer_2.textContent = response;
        setIsVisible_2(true);
      }
    }
  };

  return (
    <div className="page">
      <img src={backgroundImg} className="background" />
      <form className="form-new" onSubmit={Register}>
        <h2 className="form-header">Client Sign Up</h2>
        <div className="group">
          <label className="form-label">Username</label>
          <input
            className="form-input"
            placeholder="Enter your username"
            type="text"
            id="username"
            name="username"
            value={username}
            onChange={handleUsernameChange}
            required
          />
          <div
            id="messageContainer-1"
            className={isVisible_1 ? "visible" : "hidden"}
          ></div>
        </div>
        <div className="group">
          <label className="form-label" htmlFor="password">
            Password
          </label>
          <div className="form-password-container">
            <input
              className="form-input"
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
          <div
            id="messageContainer-2"
            className={isVisible_2 ? "visible" : "hidden"}
          ></div>
        </div>
        <button type="submit" className="form-button">
          Submit
        </button>
        {/* {<GoogleButton></GoogleButton>} */}
        <button
          type="button"
          // onClick={SignUpPage}
          // onClick={navigate("localhost:8080/SignUp/Google/Client")}
          onClick={() =>
            (window.location.href =
              "http://localhost:8080/SignUp/Google/Client")
          }
          className="googleOauth"
        >
          <img src="src\assets\google.png" className="googleIcon" />
          Sign Up with Google Account
        </button>
      </form>
    </div>
  );
}

export default ClientReg;
