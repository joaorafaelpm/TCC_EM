import React from 'react';
import './TestimonialCard.css';

const TestimonialCard = ({ userImg, userName, date, stars, text }) => {
  return (
    <div className="testimonial-card">
      <div className="card-top">
        <div className="user-info">
          {userImg ? (
            <img src={userImg} alt={userName} />
          ) : (
            <div className="avatar-letter">{userName.charAt(0)}</div>
          )}
          <div>
            <h4>{userName}</h4>
            <span>{date}</span>
          </div>
        </div>
      </div>

      <div className="stars">
        {"★".repeat(stars)}{"☆".repeat(5 - stars)}
      </div>

      <p className="testimonial-text">{text}</p>
    </div>
  );
};

export default TestimonialCard;