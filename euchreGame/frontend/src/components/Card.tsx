interface Card {
  suit: string;
  value: string;
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
 */
const Card = ({ suit, value }: Card) => {
  switch (value.toString()) {
    case "11":
      value = "J";
      break;
    case "12":
      value = "Q";
      break;
    case "13":
      value = "K";
      break;
    case "14":
      value = "A";
      break;
  }

  // opponent card - denoted by ?, and should not shown
  if (suit === "?" && value === "?") {
    return (
      <img
        src="/card_back.png"
        className="bg-red-900 border border-black rounded-lg h-[7.5rem] w-20 shadow-sm"
      />
    );
  }

  let cardColor = "";
  if (suit === "HEARTS" || suit === "DIAMONDS") {
    cardColor = "red";
  } else {
    cardColor = "black";
  }

  return (
    <div className="bg-slate-50 border border-black rounded-lg h-[7.5rem] w-20 shadow-sm transform transition-transform hover:scale-110 hover:shadow-lg hover:-translate-y-3">
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
