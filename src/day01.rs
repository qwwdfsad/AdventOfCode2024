use std::collections::HashMap;
use crate::read_lines;

pub fn solve() {
    let input = read_lines("day01.txt");
    let (mut left, mut right): (Vec<i32>, Vec<i32>) = input
        .iter()
        .map(|line| {
            let vec = line
                .split_ascii_whitespace()
                .map(|x| x.parse::<i32>().unwrap())
                .collect::<Vec<i32>>();
            (vec[0], vec[1])
        })
        .unzip();

    // Part 1
    left.sort();
    right.sort();
    let result = left.iter().zip(right.iter()).fold(0, |acc, (l, r)| {
       acc + (l - r).abs()
    });
    println!("Part 1: {}", result);

    // Part 2
    let frequencies = right.iter().fold(HashMap::new(), |mut acc, x| {
        *acc.entry(*x).or_insert(0) += 1;
        acc
    });
    let result = left.iter().fold(0, |acc, x| {
        if let Some(frequency) = frequencies.get(x) {
            acc + x * frequency
        } else {
            acc
        }
    });
    println!("Part 2: {}", result);
}
