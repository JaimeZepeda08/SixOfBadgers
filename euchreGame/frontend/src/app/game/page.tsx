import Hand from "../../components/Hand";

/**
 * Home component representing the main page of the game. Each person
 * will have a set of cards that are fanned out. All opponents will be hidden while
 * the users will be shown to them.
 * 
 * @returns {JSX.Element} The Home component.
 */
export default function Home() {
  // placeholder for user cards
  const cards = [
    { suit: "hearts", value: "A" },
    { suit: "clubs", value: "Q" },
    { suit: "spades", value: "J" },
    { suit: "diamonds", value: "K" },
    { suit: "clubs", value: "Q" },
    { suit: "spades", value: "J" },
  ];

  // placeholder for opponents cards
  const opponents = [
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
  ];

  return (
    <div className="h-screen flex justify-center items-center relative">
      <div className="absolute bottom-8" style={{ transform: "rotate(0deg)" }}>
        <Hand cards={cards} />
      </div>
      <div className="absolute left-0" style={{ transform: "rotate(90deg)" }}>
        <Hand cards={opponents} />
      </div>
      <div className="absolute top-8" style={{ transform: "rotate(180deg)" }}>
        <Hand cards={opponents} />
      </div>
      <div className="absolute right-0" style={{ transform: "rotate(270deg)" }}>
        <Hand cards={opponents} />
      </div>
    </div>
  );
}
