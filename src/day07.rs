use log::log;
use crate::read_lines;

pub fn solve() {
    let input = read_lines("day07.txt");

    let mut result = 0;
    for line in input.iter() {
        let i = line.find(":").unwrap();
        let value = line[0..i].parse::<i64>().unwrap();
        let ops = line[i + 2..]
            .trim()
            .split(" ")
            .map(|v| v.parse::<i64>().unwrap())
            .collect::<Vec<_>>();
        if try_solve(value, ops[0], 1, &ops) {
            result += value;
        }
    }

    println!("{}", result);

    let mut result = 0;
    for line in input.iter() {
        let i = line.find(":").unwrap();
        let value = line[0..i].parse::<i64>().unwrap();
        let ops = line[i + 2..]
            .trim()
            .split(" ")
            .map(|v| v.parse::<i64>().unwrap())
            .collect::<Vec<_>>();
        if try_solve2(value, ops[0], 1, &ops) {
            result += value;
        }
    }

    println!("{}", result);
}

fn try_solve2(result: i64, acc: i64, idx: usize, operands: &Vec<i64>) -> bool {
    if idx == operands.len() {
        return acc == result;
    }

    let v = operands[idx];
    let log10 = ((v as f64).log10() + 0.5).round() as i64;
    let pow = 10_i64.pow(log10.try_into().unwrap());
    try_solve2(result, acc + operands[idx], idx + 1, operands)
        || try_solve2(result, acc * operands[idx], idx + 1, operands)
        || try_solve2(result, acc * pow + operands[idx], idx + 1, operands)
}


fn try_solve(result: i64, acc: i64, idx: usize, operands: &Vec<i64>) -> bool {
    if idx == operands.len() {
        return acc == result;
    }
    try_solve(result, acc + operands[idx], idx + 1, operands)
        || try_solve(result, acc * operands[idx], idx + 1, operands)
}
