use crate::read_lines;
use colored::Colorize;
use std::collections::{HashMap, HashSet};
use std::io;
use std::process::ExitStatus;
use std::thread::sleep;
use std::time::Duration;

pub fn solve() {
    // Solution
    clr();
    println!("Solution for day 06: solve (1) or interactive (2)?");
    let mut str = String::new();
    io::stdin().read_line(&mut str).unwrap();
    match str.trim() {
        "1" => {
            let mut input: Vec<String> = read_lines("day06.txt")
                .into_iter()
                .map(String::from)
                .collect();

            solve01(&mut input, |_, _, _| ());
            solve02(&mut input, |_, _, _| ());
        }
        "2" => interactive(),
        _ => println!("See ya next time"),
    }

    // Interactive
}

fn interactive() {
    clr();
    let input: Vec<_> = "....#.....
    .........#
    ..........
    ..#.......
    .......#..
    ..........
    .#..^.....
    ........#.
    #.........
    ......#..."
        .split("\n")
        .map(|l| l.trim())
        .map(String::from)
        .collect();
    let max_y = input.len();
    let max_x = input[0].len();
    let start = start_pos(&input);
    println!(
        "Here is the starting field. Pick (y, x) coordinates to put an obstacle into. Start: {:?}",
        start
    );
    draw_no_clear(&input, start.0, start.1);
    println!();
    println!(
        "Pick two coordinates to put an obstacle in format 'y x'. Allowed range: 0..{}, 0..{}",
        max_y, max_x
    );
    let mut str = String::new();
    io::stdin().read_line(&mut str).unwrap();
    let mut parts = str.trim().split_whitespace();
    let y = parts.next().unwrap().parse::<i32>().unwrap();
    let x = parts.next().unwrap().parse::<i32>().unwrap();
    println!();
    let mut input = update_field(&input, (y, x)).expect("Invalid input");
    println!("Updated field. Press Enter to simulate");
    draw_no_clear_highlight_at(&input, start.0, start.1, (y, x));
    io::stdin().read_line(&mut str).unwrap();
    draw_no_clear(&input, start.0, start.1);
    is_cyclic(&mut input, draw);
}

fn solve02(input: &mut Vec<String>, _draw: fn(&Vec<String>, i32, i32)) {
    let max_y = input.len() as i32;
    let max_x = input[0].len() as i32;
    let mut result = 0;
    for i in 0..max_y as usize {
        for j in 0..max_x as usize {
            let mut input = input.clone();
            let nth = input[i].chars().nth(j).unwrap();
            if nth != '^' && nth != '#' {
                input[i].replace_range(j..=j, "#");
                if is_cyclic(&mut input, |_, _, _| ()) {
                    result += 1;
                }
            }
        }
    }
    println!("{}", result)
}

fn update_field(input: &Vec<String>, coordinate: (i32, i32)) -> Option<Vec<String>> {
    let mut input = input.clone();
    let y = coordinate.0 as usize;
    let x = coordinate.1 as usize;
    let nth = input[y].chars().nth(x).unwrap();
    if nth != '^' && nth != '#' {
        input[y].replace_range(x..=x, "#");
        Some(input)
    } else {
        None
    }
}

fn is_cyclic(input: &mut Vec<String>, draw: fn(&Vec<String>, i32, i32)) -> bool {
    let max_y = input.len() as i32;
    let max_x = input[0].len() as i32;

    let (mut y, mut x) = start_pos(input);
    let mut direction = 0; // multiplicative group modulo 4, up-right-down-left
    let mut visited: HashMap<(i32, i32), HashSet<i32>> = HashMap::from([((y, x), HashSet::new())]);
    loop {
        draw(input, y, x);
        let mut dx = 0;
        let mut dy = 0;
        match direction % 4 {
            0 => dy = -1,
            1 => dx = 1,
            2 => dy = 1,
            3 => dx = -1,
            _ => panic!("Invalid direction"),
        }
        let y1 = y + dy;
        let x1 = x + dx;
        if y1 < 0 || y1 >= max_y || x1 < 0 || x1 >= max_x {
            break;
        }
        if input[y1 as usize].chars().nth(x1 as usize).unwrap() == '#' {
            direction += 1;
        } else {
            y = y1;
            x = x1;
            draw(input, y, x);
            if !visited
                .entry((y, x))
                .or_insert(HashSet::new())
                .insert(direction % 4)
            {
                return true;
            }
        }
        input[y as usize].replace_range(
            x as usize..=x as usize,
            dir_to_char(direction).to_string().as_str(),
        );
    }

    false
}

fn solve01(input: &mut Vec<String>, draw: fn(&Vec<String>, i32, i32)) {
    let max_y = input.len() as i32;
    let max_x = input[0].len() as i32;

    let (mut y, mut x) = start_pos(input);
    let mut direction = 0; // multiplicative group modulo 4, up-right-down-left
    let mut visited = HashSet::from([(y, x)]);
    loop {
        draw(input, y, x);
        let mut dx = 0;
        let mut dy = 0;
        match direction % 4 {
            0 => dy = -1,
            1 => dx = 1,
            2 => dy = 1,
            3 => dx = -1,
            _ => panic!("Invalid direction"),
        }
        let y1 = y + dy;
        let x1 = x + dx;
        if y1 < 0 || y1 >= max_y || x1 < 0 || x1 >= max_x {
            break;
        }
        if input[y1 as usize].chars().nth(x1 as usize).unwrap() == '#' {
            direction += 1;
        } else {
            y = y1;
            x = x1;
            visited.insert((y, x));
        }
        input[y as usize].replace_range(
            x as usize..=x as usize,
            dir_to_char(direction).to_string().as_str(),
        );
    }

    println!("{}", visited.len());
}

fn dir_to_char(direction: i32) -> char {
    match direction % 4 {
        0 => '^',
        1 => '>',
        2 => 'v',
        3 => '<',
        _ => panic!("Invalid direction"),
    }
}

fn draw(field: &Vec<String>, y: i32, x: i32) {
    clr();
    draw_no_clear(field, y, x);
}

fn clr() -> ExitStatus {
    std::process::Command::new("clear").status().unwrap()
}

fn draw_no_clear(field: &Vec<String>, y: i32, x: i32) {
    let traced = HashSet::from(['>', '<', '^', 'v']);
    for (i, line) in field.iter().enumerate() {
        for (j, ch) in line.chars().enumerate() {
            if (i as i32) == y && (j as i32) == x {
                print!("{}", ch.to_string().red());
            } else if traced.contains(&ch) {
                print!("{}", ch.to_string().yellow());
            } else if ch == '#' {
                print!("{}", ch.to_string().on_blue());
            } else {
                print!("{}", ch);
            }
        }
        println!();
    }
    sleep(Duration::from_millis(200))
}

fn draw_no_clear_highlight_at(field: &Vec<String>, y: i32, x: i32, highlight: (i32, i32)) {
    let traced = HashSet::from(['>', '<', '^', 'v']);
    for (i, line) in field.iter().enumerate() {
        for (j, ch) in line.chars().enumerate() {
            if (i as i32) == highlight.0 && (j as i32) == highlight.1 {
                print!("{}", ch.to_string().red().bold().on_blue());
            } else if (i as i32) == y && (j as i32) == x {
                print!("{}", ch.to_string().red());
            } else if traced.contains(&ch) {
                print!("{}", ch.to_string().yellow());
            } else if ch == '#' {
                print!("{}", ch.to_string().on_blue());
            } else {
                print!("{}", ch);
            }
        }
        println!();
    }
    sleep(Duration::from_millis(200))
}

fn start_pos(input: &Vec<String>) -> (i32, i32) {
    for (y, line) in input.iter().enumerate() {
        for (x, c) in line.chars().enumerate() {
            if c == '^' {
                return (y as i32, x as i32);
            }
        }
    }
    panic!("No start position found");
}
