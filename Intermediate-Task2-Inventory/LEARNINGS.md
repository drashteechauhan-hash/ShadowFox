# Learnings - Inventory Management System

## What was the hardest bug?

When I was building the barcode search feature for Tier 2, I wanted the matching product card to flash for a second so it's obvious to the user which product got found - just scrolling to it silently felt boring and easy to miss. My first idea was to use Thread.sleep() to add a small delay between changing the card's color and changing it back.

The moment I ran it, the whole window froze. Not crashed, just completely stuck - couldn't click anything, couldn't even resize the window, until the sleep finished and then it would suddenly "catch up" all at once. I genuinely thought I'd broken something in the GUI itself at first.

## How did I fix it?

After some digging, I found out that Swing runs everything on a single thread called the Event Dispatch Thread (EDT) - this is the thread responsible for drawing the UI and listening for clicks. The moment you call Thread.sleep() on this thread, it literally pauses everything, because there's no other thread free to keep the UI responsive.

The fix was to stop using Thread.sleep() altogether and use a javax.swing.Timer instead. A Timer doesn't block anything - it just fires its action every X milliseconds on its own, without freezing the rest of the app. I set it up to toggle the card's background color a few times with a small delay between each toggle, and then stop itself automatically once it's flashed enough times.

Once I switched to Timer, the flash effect worked exactly how I wanted - smooth, and the rest of the app stayed fully clickable and responsive the whole time.

## Key Takeaway

If I ever need something to happen "after a delay" or "repeatedly for a few seconds" inside a GUI, Thread.sleep() is the wrong tool - it freezes the interface. Timer is the right way to do animations or delayed effects in Swing because it works with the EDT instead of blocking it. This was a good reminder that GUI programming isn't just about writing logic that works - it's also about understanding which thread that logic is running on.
