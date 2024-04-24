"use client";
import { SessionProvider } from "next-auth/react";

/**
 * Wrapper for the SessionProvider component from next-auth/react, allows any page to use session data.
 * @param children - React children to be wrapped by the SessionProvider.
 * @constructor
 */
export function SessionProviders({ children }: { children: React.ReactNode }) {
    return <SessionProvider>{children}</SessionProvider>;
}