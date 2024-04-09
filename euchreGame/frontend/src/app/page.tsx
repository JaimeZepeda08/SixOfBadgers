import React from "react";
import { SimpleButtonLink } from "@/components/SimpleButton";

export default function Page() {
  return (
    <div className="flex flex-col h-screen justify-center items-center">
      <SimpleButtonLink
        text="Singleplayer"
        href="home/singleplayer"
        className="my-6"
      />
      <SimpleButtonLink
        text="Multiplayer"
        href="home/multiplayer"
        className="my-6"
      />
      <SimpleButtonLink
        text="Go to Saved Games"
        href="home/saved_games"
        className="my-6"
      />
    </div>
  );
}
