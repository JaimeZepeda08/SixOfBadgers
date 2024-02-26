import Hand from "./components/Hand";

function App() {
  const cards = [
    { suit: "hearts", value: "2" },
    { suit: "diamonds", value: "K" },
    { suit: "clubs", value: "J" },
    { suit: "spades", value: "10" },
    { suit: "hearts", value: "Q" },
    { suit: "diamonds", value: "K" },
  ];

  return (
    <div className="h-screen flex justify-center items-center">
      <Hand cards={cards} />
    </div>
  );
}

export default App;
