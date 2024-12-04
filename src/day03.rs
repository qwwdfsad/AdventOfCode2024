use std::cell::RefCell;
use crate::read_lines;
use regex::Regex;

pub fn solve() {
    let input = read_lines("day03.txt");
    let regex = Regex::new(r"mul\((\d{1,3}),(\d{1,3})\)").unwrap();
    let result: i32 = input
        .iter()
        .flat_map(|line| {
            regex.captures_iter(line).map(|captures| {
                let x: i32 = captures[1].parse().unwrap();
                let y: i32 = captures[2].parse().unwrap();
                x * y
            })
        })
        .sum();

    println!("{}", result);

    // 85508223
    let regex = Regex::new(r"(do\(\))|(don't\(\))|(mul\((\d{1,3}),(\d{1,3})\))").unwrap();
    let enabled = &RefCell::new(true);
    let result: i32 = input
        .into_iter().flat_map(|line| {
            regex
                .captures_iter(&line)
                .map(move |captures| match &captures[0] {
                    "do()" => {
                        *enabled.borrow_mut() = true;
                        0
                    }
                    "don't()" => {
                        *enabled.borrow_mut() = false;
                        0
                    }
                    _ => {
                        if *enabled.borrow() {
                            let x: i32 = captures[4].parse().unwrap();
                            let y: i32 = captures[5].parse().unwrap();
                            x * y
                        } else {
                            0
                        }
                    }
                }).collect::<Vec<_>>()
        })
        .sum();

    println!("{}", result);
}
