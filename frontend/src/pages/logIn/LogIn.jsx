import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import backgroundImg from "../../assets/background.png";
import BasicSignIn from '../../components/API/signInApi';
import './LogIn.css';

const Login = () => {
  sessionStorage.setItem("hasVisitedRadialChoice", true);
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [emailError, setEmailError] = useState('')
  const [passwordError, setPasswordError] = useState('')

  const navigate = useNavigate()

  const onButtonClick = async () => {
    setEmailError('')
    setPasswordError('')

    if ('' === email) {
      setEmailError('Please enter your email')
      return
    }

    if ('' === password) {
      setPasswordError('Please enter a password')
      return
    }

    if (password.length < 7) {
      setPasswordError('The password must be 8 characters or longer')
      return
    }
    try {
      const response = await BasicSignIn(email, password);
      // Handle successful login (e.g., store token, navigate to dashboard)
      console.log("Login successful", response);
      const parsedToken = JSON.parse(response.token); 
    
      sessionStorage.setItem('token',  parsedToken.token);
      sessionStorage.setItem('role',parsedToken.role); 
      sessionStorage.setItem('id', parsedToken.id); 
      
      navigate('/home');
    } catch (error) {
      console.error("Login error:", error);
      setPasswordError(error.message); // Display error message to the user
    }
  }

  const handleSignUpClick = () => {
    navigate("/radialChoice");
  }

  return (
    <div className={'mainContainer'}>
      <img src={backgroundImg} className="background" />
      <div className={'titleContainer'}>
        <div>Login</div>
      </div>
      <br />
      <div className={'inputContainer'}>
        <input
          value={email}
          placeholder="Enter your email here"
          onChange={(ev) => setEmail(ev.target.value)}
          className={'inputBox'}
        />
        <label className="errorLabel">{emailError}</label>
      </div>
      <br />
      <div className={'inputContainer'}>
        <input
          value={password}
          placeholder="Enter your password here"
          onChange={(ev) => setPassword(ev.target.value)}
          className={'inputBox'}
        />
        <label className="errorLabel">{passwordError}</label>
      </div>
      <br />
      <div className={'inputContainer'}>
        <input className={'inputButton'} type="button" onClick={onButtonClick} value={'Sign in'} />
      </div>
      <div>
        <button
          type="button"
          onClick={() =>
          (window.location.href =
            "http://localhost:8080/auth/signin/google")
          }
          className="googleOauth"
        >
          <img src="src/assets/google.png" className="googleIcon" />
          Sign IN with Google Account
        </button>
      </div>
      <div className={'signUpContainer'}>
        <p>Don&apos;t have an account?
          <button className={'signUpButton'} onClick={handleSignUpClick}>Sign Up</button>
        </p>
      </div>
    </div>
  )
}

export default Login;
