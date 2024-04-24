import React from "react";

/**
 * Card Submission Form component.
 * 
 * @param {object} props - Component props.
 * @param {function} props.handleSubmit - Function to handle form submission.
 * @param {boolean} props.selectedCard - Flag indicating if a card is selected.
 * @returns {JSX.Element} CardSubmissionForm component.
 */
function CardSubmissionForm({ handleSubmit, selectedCard }: any) {
  return (
    <form onSubmit={handleSubmit} className="absolute bottom-0 right-0">
      <button
        type="submit"
        className={`baseButton ${
          !selectedCard ? "disabled" : ""
        } mb-20 mr-80`}
        disabled={!selectedCard}
        onClick={() => console.log("Submit action")}
      >
        Play Card
      </button>
    </form>
  );
}

export default CardSubmissionForm;
