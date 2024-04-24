/**
 * Page component providing information on how to play Euchre.
 *
 * @returns {JSX.Element} The Page component.
 */
export default function Page() {
  return (
    <div className="container mx-auto px-4 py-8">
      {/* Title */}
      <h1 className="text-3xl font-bold text-center mb-4">
        How to Play Euchre
      </h1>

      {/* What is Euchre? */}
      <h2 className="text-2xl font-semibold mb-2">What is Euchre?</h2>
      <p className="mb-4">
        Euchre is a classic trick-taking card game that originated in Europe
        but gained widespread popularity in North America. It is typically
        played with a deck of 24 cards, consisting of the 9 through Ace of each
        suit.
      </p>

      {/* How to Play */}
      <h2 className="text-2xl font-semibold mb-2">How to Play</h2>
      <p>
        Euchre is traditionally played by four players divided into two teams.
        Here are the basic steps:
      </p>
      <ol className="list-decimal ml-4 mb-4">
        <li>
          <strong>Deal:</strong> Cards are dealt clockwise until each player has
          five cards.
        </li>
        <li>
          <strong>Bidding:</strong> Starting from the player to the left of the
          dealer, each player has the option to &quot;order up&quot; the face-up
          card as the trump suit by saying &quot;I order it up&quot; or to pass by
          saying &quot;Pass.&quot; If all players pass, the dealer has the option
          to pick up the card themselves and choose the trump suit or pass
          again, leading to a new round of bidding.
        </li>
        <li>
          <strong>Gameplay:</strong> If a player successfully orders up the
          face-up card or if the dealer picks it up, the suit of that card
          becomes the trump suit. The player who ordered or picked up the
          card&apos;s suit becomes the &quot;maker&quot; and plays alone against
          the other three players.
          <ol className="list-decimal ml-10">
            <li style={{ listStyleType: "lower-alpha" }}>
              The player to the left of the dealer leads the first trick by
              playing any card from their hand.
            </li>
            <li style={{ listStyleType: "lower-alpha" }}>
              Players must follow suit if possible; if they can&apos;t, they can
              play any card.
            </li>
            <li style={{ listStyleType: "lower-alpha" }}>
              The highest card of the led suit or the highest trump card wins
              the trick.
            </li>
            <li style={{ listStyleType: "lower-alpha" }}>
              The winner of each trick leads the next one.
            </li>
          </ol>
        </li>
        <li>
          <strong>Scoring:</strong> The makers aim to win at least three tricks
          to score points. If they succeed, they earn one point. If they win all
          five tricks, they earn two points. If the makers fail to win three
          tricks, they are &quot;euchred,&quot; and the opposing team earns two
          points.
        </li>
        <li>
          <strong>Wining:</strong> The game typically continues until one team
          reaches a predetermined score, often 10 points, to win.
        </li>
      </ol>

      {/* Tips and Strategies */}
      <h2 className="text-2xl font-semibold mb-2">Tips and Strategies</h2>
      <ul className="list-disc ml-4 mb-4">
        <li>Communication with your partner is crucial.</li>
        <li>Pay attention to the trump suit and the strength of your hand.</li>
        <li>
          Remember which cards have been played to make informed decisions.
        </li>
      </ul>
    </div>
  );
}
