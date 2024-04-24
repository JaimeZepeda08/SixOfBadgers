import React from "react";

/**
 * Page Component
 * 
 * This component represents a simple page layout for the "Singleplayer" mode.
 * It includes a red-colored background with a heading "Singleplayer".
 */
export default function Page() {
  return (
    <div
      className="flex flex-col items-center justify-center mt-5"
      style={{ zoom: 0.8 }}
    >
      <div className="bg-red-500 text-white text-4xl p-4 rounded-lg mb-4">
        <h1>Singleplayer</h1>
      </div>
    </div>
  );
}
