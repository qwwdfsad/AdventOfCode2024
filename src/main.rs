use std::io::BufRead;
use crate::day04::solve;

mod day04;

fn read_lines(file: &str) -> Vec<String> {
    let file = std::fs::File::open("/Users/qwwdfsad/Desktop/aoc/".to_owned() + file).unwrap();
    let reader = std::io::BufReader::new(file);
    reader.lines().map(|l| l.unwrap()).collect()
}

fn main() {
    solve();
}
