const Card = ({ suit, value }) => {
  let cardColor = "";
  if (suit === "hearts" || suit === "diamonds") {
    cardColor = "red";
  } else {
    cardColor = "black";
  }

  return (
    <div className="bg-slate-50 border border-black rounded-lg h-32 w-20 shadow-sm transform transition-transform hover:scale-110 hover:shadow-2xl hover:-translate-y-3">
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
        {suit === "hearts" && "♥"}
        {suit === "diamonds" && "♦"}
        {suit === "clubs" && "♣"}
        {suit === "spades" && "♠"}
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