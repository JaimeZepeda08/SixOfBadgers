interface Card {
  suit: string;
  value: string;
  isSelected?: boolean;
}

/**
 * Card Component
 *
 * Renders a visual representation of a playing card. This component is designed to display both the suit and value of a card.
 * It also includes styling for different card suits (hearts, diamonds, clubs, spades) and a special case for an "opponent's card",
 * which is not shown to the player (represented by a suit and value of "?").
 *
 * @param {object} Card The component props
 * @param {string} Card.suit The suit of the card. Expected values: "hearts", "diamonds", "clubs", "spades", or "?" for an opponent's hidden card.
 * @param {string} Card.value The value of the card. Can be "9", "10", "J", "Q", "K", "A" or "?" for an opponent's hidden card.
 * @returns HTML representing a card
 *
 * @author jaime zepda
 */
const Card = ({ suit, value, isSelected }: Card) => {
  // opponent card - denoted by ?, and should not shown
  if (suit === "?" && value === "?") {
    return (
      <div className="bg-red-900 border border-black rounded-lg h-32 w-20 shadow-sm transform transition-transform hover:scale-110 hover:shadow-2xl hover:-translate-y-3"></div>
      // <div className="bg-red-900 border border-black rounded-lg h-32 w-20"></div>
    );
  }

  const applyHoverStyles = "scale-110 shadow-2xl -translate-y-3 border border-red-700 rounded-lg";

  let cardColor = "";
  if (suit === "HEARTS" || suit === "DIAMONDS") {
    cardColor = "red";
  } else {
    cardColor = "black";
  }

  return (
    <div className={`bg-slate-50 h-32 w-20 shadow-sm transform transition-transform ${isSelected ? applyHoverStyles : 'hover:scale-110 hover:shadow-2xl hover:-translate-y-3 border border-black rounded-lg'}`}>
      <div
        className={`absolute top-0 left-0 text-lg font-bold mt ml-1 text-${cardColor}-600`}
      >
        {value}
      </div>

      <div
        className={`flex justify-center items-center text-5xl ${
          cardColor === "red" ? "text-red-600" : "text-black"
        }`}
        style={{
          transform: `translateY(75%) `,
        }}
      >
        {suit === "HEARTS" && "♥"}
        {suit === "DIAMONDS" && "♦"}
        {suit === "CLUBS" && "♣"}
        {suit === "SPADES" && "♠"}
      </div>

      <div
        className={`absolute bottom-0 right-0 text-lg font-bold mb mr-1 text-${cardColor}-600`}
      >
        {value}
      </div>
    </div>
  );
};

export default Card;
