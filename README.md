# NASA_Map_Game
==== Description of the design implements the required common APIs. ====
 
The API has three main interfaces IUser, IChatRoom and IReceiver. IUser is for local user, IChatRoom is for
the chat rooms the users are in. Each chat room can be regarded as a communication cycle. Users in different 
chat rooms communicate separately, so each of these combination has a IReceiver for sending and accepting 
messages. 

We design a game application based on this architecture. Initially, users are connected using IP address to 
get the remote registry. And from the remote registry, a user gets the remote user stub of another user, then
a user can send a game lobby invitation to another user or to request to join a game lobby from another user.
The game lobby implements the chat room interface.

After users join a game lobby created by a creator user. The creator user can send an install game data packet 
containing a game lobby MVC factory to users in the lobby. At this step, the request and install unknown commands 
functionality will be used. Users received the game MVC factory and install the game by instantiates the game controller
locally. When creating the game MVC, it will create a game chat room, and several predefined team chat room. 
And we also include the local user(who is creating the game MVC) in the install game packet, the game MVC can 
also creates receivers for the game and team chat rooms using the local user's stub. In this way, we created a 
virtual environment of the game by creating the game MVC. And later everything is synchronized in the game MVC
by sending game specific synchronization data packets.


==== System Architecture: ====
1. MVC Pattern.
The chat app, chat room, game are all MVC patterns.
2. Extended Visitor pattern
Extended Visitor pattern are used for data packet handling.
3. Inheritance-based pattern
Our program is designed based on the common API. Our objects extends the interfaces in the API.

==== Description of how the game works, including game play with winning and losing. ====

Our game is a turn-based strategy. The whole game has 10 rounds. After everyone chooses a team and
ready, the game begins. Every player and his teammates will have a base city. Player have some soldiers
can be allocated to the cities around your team to occupy cities. Left clicking the city on the map is 
adding soldiers in the city and right clicking the city on the map is dropping soldiers in the city.
During the game, every player can see the cities that your team occupies or neighborhood cities.
Player can also see some informations about those city like city resource, the soldiers you or your
teammates allocate in the city. After allocating the soldiers, player can click ready for next round
button to show you are ready for next round (we have time limit (60 seconds) for every round). 
If every player is ready for next round. The game will fight. The team who allocate most soldiers
in the city will occupy the city and gain the resource that can transfer to your soldiers.
Finally, the team which occupies the most cities will win the game.

==== The design of our game ====
1. There are multiple cities on the map.
2. each city has resources
3. many teams can put army in a city
4. after all teams are ready, there will be fighting in the city.
5. the team with largest amount of army in the city will win.
6. other teams' armies in this city will vanish.
7. the resource will be converted into the winner's army.
8. user can transfer army from one city to another.
9. When a user occupy all the cities or after certain number of rounds, the game will finish.
10. The AI is a special kind of user, and it has AI algorithms to be implemented.

==== User Manual ====
How to start the program, both the program itself as well as any individual games or processes.
please click the MainController (4).launch to start the chat app.
Player can input a user name and click Log in button to log in.
When player log in, player will create a lobby chatroom for the game.
When other players connect to the host player, they will be invited to the lobby.
They can click accept to join the lobby. In the lobby, every can chat with each other.
Now the host player can click Start Game button to send game to every one in the lobby.
A new game window will appear. 
Now the every one can choose team and click Join and Ready.
Now player can send message to his teammates by click Send to Team button.
And player can send message to everyone by click Send to Game button.
When everyone is ready the game will start.

How to connect to other users
After the player logins in, the player can input IP to connect other.
If the player did not get invitation automatically from the connecting user,
you can choose the chatroom and click Join Chat Room to join other's chatroom. 
Other users can also connect to the player by IP. When they connect to the player,
they will be invited to the player's lobby chatroom automatically.

How to use every feature of the program. 
When the game starts, on the top panel of game window, the soldiers that the player can 
allocate is shown. The round number and remain time for the round will also be shown.
Player can click "Ready for Round" button when you finish operations for the round.
Below the button, number of players that are ready for the round will be shown.
On the map, the cities that your team occupies and the neighborhood cities will be shown.
Players can letf click the city to add troops in the city or right click to remove troops 
in the city. Players can also use the feature of the map to help your operation during
the game.

How to end and exit the program. 
When the user want to leave the chat app, please click "Exit Program" button.
When the user want to leave the chat room, please click "Exit Room" button.
When the game finish, you can close the game window to leave the game.
