import Link from "next/link";

export default function Page() {
  return (
    <div className="flex justify-center items-center h-screen">
      <Link
        href="/home/saved_games"
        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
      >
        Go to Saved Games
      </Link>
    </div>
  );
}
