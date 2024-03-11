import Link from 'next/link';
import { MdMore } from "react-icons/md";
import { FaDatabase } from "react-icons/fa6";

export default function Page() {
    // saved games data that will eventually be grabbed from the backend
    const games = [
        {
          "gameNumber": 1,
          "date": "2024-03-01",
          "winLoss": 1,
          "gameTime": "19:00",
          "points": 102
        },
        {
          "gameNumber": 2,
          "date": "2024-03-05",
          "winLoss": 0,
          "gameTime": "20:30",
          "points": 95
        },
        {
          "gameNumber": 3,
          "date": "2024-03-08",
          "winLoss": 1,
          "gameTime": "18:45",
          "points": 110
        },
        {
          "gameNumber": 4,
          "date": "2024-03-12",
          "winLoss": 0,
          "gameTime": "19:15",
          "points": 98
        },
        {
          "gameNumber": 5,
          "date": "2024-03-16",
          "winLoss": 1,
          "gameTime": "20:00",
          "points": 105
        }
      ]
      
    return (
        // creates a grid of saved games
        <div className="bg-red-500 h-screen border-2 border-black mt-2">
          <div className="bg-white text-black py-2 px-4 font-bold">
          {/* header for all the data */}
            <div className="grid grid-cols-6">
              <div className="border-r border-black pr-4">Game #</div>
              <div className="border-r border-black pr-4">Date</div>
              <div className="border-r border-black pr-4">W/L</div>
              <div className="border-r border-black pr-4">Game Time</div>
              <div className="border-r border-black pr-4">Points</div>
              <div ><FaDatabase /></div>
            </div>
        </div>
        {/* map all data out, a game for every row and a button to access more data later on */}
        {games.map((game, index) => (
          <div key={index} className="border-t border-2 border-black bg-red-300 text-black py-2 px-4">
            <div className="grid grid-cols-6">
              <div>{game.gameNumber}</div>
              <div>{game.date}</div>
              <div>{game.winLoss === 1 ? 'W' : 'L'}</div>
              <div>{game.gameTime}</div>
              <div>{game.points}</div>
              <Link href="/">
                <MdMore /> 
              </Link>
            </div>
          </div>
        ))}
      </div>
    );
}