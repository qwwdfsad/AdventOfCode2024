use crate::read_lines;

pub fn solve() {
    let input = read_lines("day02.txt");
    let result = input
        .iter()
        .map(|line| {
            line.split_ascii_whitespace()
                .map(|x| x.parse::<i32>().unwrap())
                .collect::<Vec<i32>>()
        })
        .filter(|levels| is_safe(levels))
        .count();

    println!("{}", result);

    let result = input
        .iter()
        .map(|line| {
            line.split_ascii_whitespace()
                .map(|x| x.parse::<i32>().unwrap())
                .collect::<Vec<i32>>()
        })
        .filter(|levels| {
            for i in 0..levels.len() {
                let mut levels = levels.clone();
                levels.remove(i);
                if is_safe(&levels) {
                    return true;
                }
            }
            is_safe(levels)
        })
        .count();

    println!("{}", result);
}

fn is_safe(levels: &Vec<i32>) -> bool {
    let mut sorted = levels.clone();
    sorted.sort();
    if !equal(levels, &sorted) {
        sorted.sort_by(|a, b| b.cmp(a));
        if !equal(levels, &sorted) {
            return false;
        }
    }
    zip_with_next(levels).all(|(a, b)| {
        let d = (a - b).abs();
        d >= 1 && d <= 3
    })
}

fn zip_with_next(vec: &Vec<i32>) -> impl Iterator<Item = (i32, i32)> + '_ {
    let iter = vec.into_iter().cloned();
    let next =  iter.clone();
    iter.zip(next.skip(1))
}

fn equal(v1: &Vec<i32>, v2: &Vec<i32>) -> bool {
    for i in 0..v1.len() {
        if v1[i] != v2[i] {
            return false;
        }
    }
    true
}
