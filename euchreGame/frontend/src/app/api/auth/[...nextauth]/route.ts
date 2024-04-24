import GoogleProvider from 'next-auth/providers/google';
import NextAuth from "next-auth";

/**
 * NextAuth handler for Google OAuth. Gives GoogleProvider with the client ID and secret
 * Technically handles GET and POST request between client and Google OAuth to authorize user.
 * Data will then get sent to backend for storage in db once retrieved.
 */
const handler = NextAuth({
    providers: [
        GoogleProvider({
            clientId: process.env.GOOGLE_ID as string,
            clientSecret: process.env.GOOGLE_SECRET as string,
        }),
    ],
});
// @ts-ignore
export {handler as GET, handler as POST}