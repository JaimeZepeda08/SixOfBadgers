import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import TopNav from "@/components/TopNav";
import MusicPlayer from "@/components/MusicPlayer";
import Profile from "@/components/Profile";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Euchre With Friends",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <TopNav
          components={[<Profile key="profileComponent"/>, <MusicPlayer key="musicPlayerComponent" src="/pokerFace.mp3" />]}
        />
        {children}
      </body>
    </html>
  );
}
