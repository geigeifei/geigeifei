#ifndef PLAGIARISMCHECKER_H
#define PLAGIARISMCHECKER_H

#include <string>
#include <vector>
#include <map>

class PlagiarismChecker {
public:
	// ���캯������ʼ��ԭ�ĺͳ�Ϯ�����ĵ�·��
	PlagiarismChecker(const std::string& origFile, const std::string& plagFile);

	// �������ƶȲ�������
	void calculateAndSaveSimilarity(const std::string& outputFile);

private:
	// ��ȡ�ļ����ݣ�֧��UTF-8���룩
	std::string readFile(const std::string& filePath);

	// ���ı����ַ��ִ�
	std::vector<std::string> tokenize(const std::string& text);

	// �����Ƶ��TF��
	std::map<std::string, int> computeTF(const std::vector<std::string>& words);

	// �������ĵ�Ƶ�ʣ�IDF��
	std::map<std::string, double> computeIDF(const std::vector<std::vector<std::string>>& documents);

	// ����TF-IDF����
	std::vector<double> computeTFIDF(const std::vector<std::string>& words,
		const std::map<std::string, int>& tf,
		const std::map<std::string, double>& idf);

	// �����������ƶ�
	double cosineSimilarity(const std::vector<double>& vec1, const std::vector<double>& vec2);

	// ԭ�ĺͳ�Ϯ�����ĵ�����
	std::string origText;
	std::string plagText;
};

#endif // PLAGIARISMCHECKER_H