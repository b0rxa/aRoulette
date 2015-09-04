# aRoulette
A simple graphical program to simulate a roulette used to randomly select students.

I created this program to implement a procedure to select students to solve problems in the blackboard. The main idea behind this procedure is that the probability of being selected is modified according to the results. When a student refuses to solve a problem, then its probability of being selected is increased. Conversely, when a student volunteers its probability of being selected is decreased. 

The probabilities are graphically shown as a biased roulette and the program simulates draws of the rulette; the simulation can be modified to through the parameters of the program.

## Basic use

The use of the program is very simple. First of all, we have to load a list of students (file -> load list). It has to be a file with one student per row (without empty rows). It can contain just the student's name or, after a comma, the points associated to the student (the more points, the less the probability of being chosen). 

Once the list is loaded, we can perform two tasks: run the roulette or update the points.

### Running the roulette

By default, all the students will be selected. However, for different reasons we may want to have a shorter list. It can be simply achieved by selecting some of the students in the list.

Then, the 'run' button will make the roulette start moving. After a while, it will stop indicating the student selected. It can be manually stopped using the 'stop' button.

The behaviour of the roulette (acceleration, inertia, etc.) can be configured in the 'Roulette' tab of the configuration window (File -> Options).

### Updating the roulette

The puntuation associated to a student can be updated with the 'Update' button and the combo box to its left. There are four situations considered: 

-**Volunteer**: When a student decides to solve the exercise without being selected by the roulette, its puntuation is increased the most, +2 points.

-**Picked up & done**: When a student is selected and solves the exercise its puntuation is increased whith 1 point.

-**Picked up & done**: When a student is selected but does not solve the exercise, its puntuation remains unmodified.

-**Refussed**: When a student is selected but refusses solving the problem, its puntuation is decreased in 1 point.

In future versions, the update associated to each event will be configurable.

In case the student to be updated is not the one selected by the roulette, it can be manually set by selecting it in the combo box with the list of student and pushing the button to its right.

In case the list is modified, it can be saved (File -> Save List).