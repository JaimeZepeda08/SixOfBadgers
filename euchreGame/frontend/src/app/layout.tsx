import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import TopNav from "@/components/TopNav";
import MusicPlayer from "@/components/MusicPlayer";
import { SocketProvider } from "@/lib/SocketProvider";
import {SessionProviders} from "@/lib/SessionProviders";
import GoogleSignInButton from "@/components/GoogleSignInButton";
import {UserProvider} from "@/lib/UserContext";

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
        <SessionProviders>
            <UserProvider>
                <TopNav
                    components={[<MusicPlayer src="/pokerFace.mp3" key={"Music Player"}/>, <GoogleSignInButton key={"SignIn"}/>]}
                />
                {children}
            </UserProvider>
        </SessionProviders>
      </SocketProvider>
      </body>
    </html>
  );
}
