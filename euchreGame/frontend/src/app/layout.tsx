import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import TopNav from "@/components/TopNav";
import MusicPlayer from "@/components/MusicPlayer";
import Profile from "@/components/Profile";
import { SocketProvider } from "@/lib/SocketProvider";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Euchre With Friends",
};

/**
 * Root Layout Component
 * 
 * This component represents the root layout of the application, providing common structure
 * such as top navigation, music player, and profile icon. It also sets up global styles
 * and language settings.
 */
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <SocketProvider>
          <TopNav
            components={[<MusicPlayer key="musicPlayer" src={"/pokerFace.mp3"} />, <Profile key={"profile"}/>]}
          />
          {children}
        </SocketProvider>
      </body>
    </html>
  );
}
