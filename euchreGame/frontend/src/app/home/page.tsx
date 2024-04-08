import React from "react";
import Link from "next/link";

export default function Page() {
  return (
    <div className="flex h-screen justify-center items-center flex-col">
      <div className="my-8">
        <Link
          href="home/singleplayer"
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Singleplayer
        </Link>
      </div>
      <div className="my-8">
        <Link
          href="home/multiplayer"
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Multiplayer
        </Link>
      </div>
      <div className="my-8">
        <Link
          href="home/saved_games"
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Go to Saved Games
        </Link>
      </div>
    </div>
  );
}
