/**
 * This class stores types needed to work with data in the game
 */

/**
 * Type for user data to be used within userContext
 */
export interface userType {
    userName: any; // name from Google account
    email: any; // email from Google account
    image: any; // image from Google account
}

/**
 * Type for game data to be used the userType to store a list of reports as saved games in userContext
 */
export type reportType = {
    startTime: Date;
    endTime: Date;
    players: string[];
    score: number[];
    gameUID: number,
};