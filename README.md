# Claim!
Claim! is a turn-base playing cards game that can be played with 2-5 players. The main goal is to have the lowest sum at the end of the match. Each card has assigned a score (A = 1, 2 = 2, ..., Q = 13, K = 14). When your turn arrives, you can drop a card or a set of cards with same rank and draw the top 1 card from the deck or from the thrown deck. There's 1 different trump for each match. Trump's score overrides the real score and equals to 0 in that match. The game continues untill one of the players considers he has the lowest sum of scores in hand. In that moment, he can call "Claim!". If his score is the lowest, the claimer will win, and everybody else will lose. If not, all players that have the sum of scores lowest or equal with the claimer will win and the claimer with all the players that have the sum of scores greater than the claimer, will lose.

# Installation
Open pom.xml as a project.

# Features
- Client-Server architecture
- Server SOLID architecture running springboot for tomcat server and also for websocket
- Database linked to server thru Hibernate (auto create if it does not exists) - set database credentials in *server/../resources/application.properties*
- Client frame-based rendering architecture (components called on update/draw methods each frame) running springboot for tomcat server
- The game is rendered into HTML5 Canvas using javascript (fully compatible and tested with Chrome)
- Other pages (login/register) uses bootstrap for layout
- Profile picture system - user can choose from a picture pool - 9 male avatars, 9 female avatars and 1 gender-neutral avatar
- Lobby system 
  - user can create a new private lobby in order to play with his friends only 
  - the creator of the lobby will become the 'host' of the lobby - the only who can start the match and or choose the lobby's visibility
  - other users can join via lobby's access code which is visible only inside the lobby (if the lobby is private) or join on the home page (if the lobby is public)
  - if the host of the lobby leaves, a new host will be picked; if there's no one to pick, the lobby will be deleted
- Game Management
  - when the game starts, a fresh game state will be created and asociated with the lobby
  - when a user finishes his move, the game state will advance to a new state, based on user's choises
  - when a user claims, the game will compute rankings and display them to all users available in game
  - after the game leaves the ended match, they will be redirected back to their lobby and from there they can choose to play again or to leave the lobby
  - while in game, no player can leave, only in a force manner (closes the browser etc) - in this case, the player will be removed from the game after a recalibration:
    - if it was the leaving player's turn - the turn will be passed to the next player
    - leaving player cards will be added into the thrown deck cards
    - leaving player's loses will be increased
    - if there is no player in the game to continue, the lobby is deleted

# More to come
I will drop my support in this project, since I am planning to release a multi-game platform for playing cards games only, based on this project.

# Questions?
Feel free to contact me for any questions.
