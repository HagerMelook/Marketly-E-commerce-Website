import { useEffect, useState } from 'react';
import Confetti from 'react-confetti';
import { useNavigate } from "react-router-dom";
import './CheckoutComplete.css';

const CheckoutComplete = () => {
    const navigate = useNavigate();
    const [isConfettiVisible, setIsConfettiVisible] = useState(true);
    const order_number =  sessionStorage.getItem('order_number');
    const total =  sessionStorage.getItem('total');

    useEffect(() => {
        const hasVisitedRadialChoice = localStorage.getItem(
            "hasVisitedRadialChoice"
        );
        if (!hasVisitedRadialChoice) {
            navigate("/");
        }
    }, [navigate]);

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsConfettiVisible(false);
        }, 500000);

        return () => clearTimeout(timer);
    }, []);

    return (
        <div className="checkout-complete">
            <Confetti
                width={window.innerWidth}
                height={window.innerHeight}
                run={isConfettiVisible}
            />
            <div className="checkout-complete-content">
                <h1 className="checkout-complete-thank-you">Thank You for Your Order!</h1>
                <p className="checkout-complete-message">Your order has been placed successfully.</p>
                <p className="checkout-complete-message">We are preparing it for you!</p>
                <div className="checkout-complete-order-summary">
                    <h2>Order Summary</h2>
                    <ul>
                        <li><strong>Order Number:</strong> {order_number}</li>
                        <li><strong>Total:</strong> {total}</li>
                        <li><strong>Estimated Delivery:</strong> 3-5 Business Days</li>
                    </ul>
                </div>
                <button className="checkout-complete-continue-button" onClick={() => navigate("/home")}>Continue Shopping</button>
            </div>
        </div>
    );
};

export default CheckoutComplete;
