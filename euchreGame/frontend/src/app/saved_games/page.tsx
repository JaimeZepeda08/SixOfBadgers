'use client';
import {useSession} from "next-auth/react";
import React, {useContext} from "react";
import {signInGoogle} from "@/components/GoogleSignInButton";
import {UserContext} from "@/lib/UserContext";


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
 * the game's details.
 */
export default function Page() {
  // saved games data that will eventually be grabbed from the backend
  // const records = getGameRecords();
  const {status: status } = useSession();
  const {
    savedGames,
  } = useContext(UserContext);

  if(status === "authenticated") {
    if(savedGames && savedGames.length === 0) {
      return (
          <div
              className="flex flex-col justify-center items-center"
              style={{ zoom: 1.5 }}
          >
            <div className={'border-4 border-red-500 rounded-md shadow-md mt-48 py-2 px-3'}>
              <h1 className={'text-red-500 font-bold'}>No saved Games</h1>
            </div>
          </div>
      );
    } else {
    return (
        // creates a grid of saved games
        <div className="bg-white h-screen border-black mt-2">
          <div className="bg-white border-black border-b-2 py-2 px-4 font-bold">
            {/* header for all the data */}
            <div className="grid grid-cols-5">
              <div className="border-r border-black pr-4">Game #</div>
              <div className="border-r border-black pr-4">Date</div>
              <div className="border-r border-black pr-4">Your Team Score</div>
              <div className="border-r border-black pr-4">Enemy Team Score</div>
              <div className="border-r border-black pr-4">Game Time</div>
              <div className="">Points</div>
            </div>
          </div>

          {/* map all data out for each col*/}
          {savedGames.map((game, index) => (
              <div
                  key={index}
                  className="border-t border-2 border-black bg-red-300 text-black py-2 px-4"
              >
                {/* display data */}
                <div className="grid grid-cols-5">
                  <div>{index}</div>
                  <div>{game.startTime.getDate()}</div>
                  <div>{game.scores[0]}</div>
                  <div>{game.scores[1]}</div>
                  <div>{Math.abs(game.endTime.getTime() - game.startTime.getTime())}</div>
                </div>
              </div>
          ))}
        </div>
    );
  }
    }
  else {
    return (
        <div
            className="flex flex-col justify-center items-center"
            style={{ zoom: 1.5 }}
        >
          <div onClick={signInGoogle}
               className={'border-4 border-red-500 rounded-md shadow-md mt-48 py-2 px-3 transition-colors duration-200 ' +
              'ease-in-out group hover:bg-red-500 hover:shadow-lg my'}>
            <h1 className={'text-red-500 font-bold group-hover:text-white'}>Please Sign In To View Saved Games</h1>
          </div>
        </div>
    );
  }
}


