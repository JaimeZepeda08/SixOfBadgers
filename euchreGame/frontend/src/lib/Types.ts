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
* Type for saved game data to be used the userType to store a list of reports as saved games in userContext
*/
export type gameRecord = {
    startTime: Date; // time the game started
    endTime: Date; // time the game ended
    players: string[]; // players[0] = you, players[1] = ally, players[2] = enemy1, players[3] = enemy2
    scores: number[];  // scores[0] = your team score, scores[1] = enemy team score
    gameUID: number; // unique identifier for the game
}