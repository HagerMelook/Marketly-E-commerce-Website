import { useState } from 'react';
import './FeedbackForm.css';
import Footer from '../../components/footer/Footer.jsx';
import Header from '../../components/header/Header.jsx';

function FeedbackForm() {
  const [feedback, setFeedback] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false); 

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (feedback.trim().length < 10) {
      setError(`Feedback must be at least 10 characters long`);
      return;
    }

    setLoading(true);
    setError('');
    setSubmitted(false);

    try {
      const response = await fetch('https://localhost:8080/add-feedback', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${sessionStorage.getItem("token")}`,
        },
        body: JSON.stringify({ feedback }),
      });

      if (!response.ok) {
        throw new Error('Failed to submit feedback. Please try again.');
      }

      setSubmitted(true);
      setFeedback('');
    } catch (err) {
      setError(err.message || 'Something went wrong. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
    <div className="feedback-page">
      <Header />
      <div className="feedback-container">
        <h2>Share Your Feedback</h2>
        <form className="feedback-form" onSubmit={handleSubmit}>
          <textarea
            className="feedback-textarea"
            value={feedback}
            onChange={(e) => setFeedback(e.target.value)}
            placeholder="Please share your thoughts..."
            maxLength={500}
          />
          <div>
            <button
              className="submit-button"
              type="submit"
              disabled={!feedback.trim() || loading}
            >
              {loading ? 'Submitting...' : 'Submit Feedback'}
            </button>
          </div>

          {submitted && (
            <div className="feedback-message success">
              Thank you for your feedback!
            </div>
          )}

          {error && (
            <div className="feedback-message error">
              {error}
            </div>
          )}
        </form>
      </div>
    </div>
    <Footer />
    </>
  );
}

export default FeedbackForm;