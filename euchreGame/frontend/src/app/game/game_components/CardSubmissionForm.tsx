import React from "react";

function CardSubmissionForm({ handleSubmit, selectedCard }) {
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
