import Link from "next/link";
import { getGameRecords } from "@/lib/userService";


/**
 * Page component displays a list of saved game records.
 *
 * This component fetches and displays game records, including details such as game number, date,
 * win/loss status, game time, and points. It presents the games in a grid, ordered from most
 * recent to oldest. Each game is associated with a unique game ID.
 *
 * Note: Future implementations should replace the hardcoded data with dynamic data fetched
 * from a backend service, potentially using the commented `getGameRecords` function.
 *
 * @returns {JSX.Element} The component renders a grid of saved games, with each row
 * representing a game. It includes a header row for labels and multiple game rows, each displaying
 * the game's details. Currently, there is no interaction logic implemented for accessing more
 * detailed data about each game.
 */
export default function Page() {
  // saved games data that will eventually be grabbed from the backend
  // const records = getGameRecords();
  const games = [
    {
      gameNumber: 1,
      date: "2024-03-01",
      winLoss: 1,
      gameTime: "19:00",
      points: 102,
    },
    {
      gameNumber: 2,
      date: "2024-03-05",
      winLoss: 0,
      gameTime: "20:30",
      points: 95,
    },
    {
      gameNumber: 3,
      date: "2024-03-08",
      winLoss: 1,
      gameTime: "18:45",
      points: 110,
    },
    {
      gameNumber: 4,
      date: "2024-03-12",
      winLoss: 0,
      gameTime: "19:15",
      points: 98,
    },
    {
      gameNumber: 5,
      date: "2024-03-16",
      winLoss: 1,
      gameTime: "20:00",
      points: 105,
    },
  ];

  return (
    // creates a grid of saved games
    <div className="bg-red-500 h-screen border-2 border-black mt-2">
      <div className="bg-white text-black py-2 px-4 font-bold">
        {/* header for all the data */}
        <div className="grid grid-cols-5">
          <div className="border-r border-black pr-4">Game #</div>
          <div className="border-r border-black pr-4">Date</div>
          <div className="border-r border-black pr-4">W/L</div>
          <div className="border-r border-black pr-4">Game Time</div>
          <div className="">Points</div>
        </div>
      </div>
      {/* map all data out for each col*/}
      {games.map((game, index) => (
        <div
          key={index}
          className="border-t border-2 border-black bg-red-300 text-black py-2 px-4"
        >
          <div className="grid grid-cols-5">
            <div>{game.gameNumber}</div>
            <div>{game.date}</div>
            <div>{game.winLoss === 1 ? "W" : "L"}</div>
            <div>{game.gameTime}</div>
            <div>{game.points}</div>
          </div>
        </div>
      ))}
    </div>
  );
}
